package expo.modules.launchpad

import androidx.compose.runtime.*
import java.math.BigDecimal
import java.math.RoundingMode

enum class UnitEnum(val baseUnit: BaseUnit, val factor: BigDecimal) {
    // Time
    HOUR(BaseUnit.SECOND, BigDecimal("3600")),
    MINUTE(BaseUnit.SECOND, BigDecimal("60")),
    SECOND(BaseUnit.SECOND, BigDecimal("1")),
    MILLISECOND(BaseUnit.SECOND, BigDecimal("0.001")),

    // Length
    KILOMETER(BaseUnit.METER, BigDecimal("1000")),
    METER(BaseUnit.METER, BigDecimal("1")),
    CENTIMETER(BaseUnit.METER, BigDecimal("0.01")),
    MILLIMETER(BaseUnit.METER, BigDecimal("0.001")),
    MILE(BaseUnit.METER, BigDecimal("1609.34")),
    YARD(BaseUnit.METER, BigDecimal("0.9144")),
    FOOT(BaseUnit.METER, BigDecimal("0.3048")),
    INCH(BaseUnit.METER, BigDecimal("0.0254")),

    // Mass
    KILOGRAM(BaseUnit.GRAM, BigDecimal("1000")),
    GRAM(BaseUnit.GRAM, BigDecimal("1")),
    POUND(BaseUnit.GRAM, BigDecimal("453.592")),
    OUNCE(BaseUnit.GRAM, BigDecimal("28.3495")),
    TONNE(BaseUnit.GRAM, BigDecimal("1000000")),

    // Volume
    LITER(BaseUnit.CUBIC_METER, BigDecimal("0.001")),
    MILLILITER(BaseUnit.CUBIC_METER, BigDecimal("0.000001")),
    CUBIC_METER(BaseUnit.CUBIC_METER, BigDecimal("1")),
    GALLON_US(BaseUnit.CUBIC_METER, BigDecimal("0.00378541")),

    CELSIUS(BaseUnit.KELVIN, BigDecimal("1")),
    FAHRENHEIT(BaseUnit.KELVIN, BigDecimal("0.5555555555555556")), // 5/9
    KELVIN(BaseUnit.KELVIN, BigDecimal("1"));

    enum class BaseUnit {
        SECOND,
        METER,
        GRAM,
        CUBIC_METER,
        KELVIN
    }

    fun displayName(): String {
        return this.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

/**
 * Converts a value from one unit to another.
 *
 * @param value The numerical value to convert.
 * @param fromUnit The unit to convert from.
 * @param toUnit The unit to convert to.
 * @param scale The number of decimal places for the result.
 * @return The converted value, or null if conversion is not possible (e.g., incompatible base units).
 */
fun convertUnits(
    value: BigDecimal,
    fromUnit: UnitEnum,
    toUnit: UnitEnum,
    scale: Int = 5 // Default scale
): BigDecimal? {
    if (fromUnit.baseUnit != toUnit.baseUnit) {
        return null
    }

    if (fromUnit.baseUnit == UnitEnum.BaseUnit.KELVIN) {
        return convertTemperature(value, fromUnit, toUnit, scale)
    }

    val valueInBaseUnit = value.multiply(fromUnit.factor)

    val result = valueInBaseUnit.divide(toUnit.factor, scale, RoundingMode.HALF_UP)
    return result
}

/**
 * Handles temperature conversions specifically, as they involve additive constants.
 */
private fun convertTemperature(
    value: BigDecimal,
    fromUnit: UnitEnum,
    toUnit: UnitEnum,
    scale: Int
): BigDecimal {
    // Convert to Kelvin first
    val kelvinValue = when (fromUnit) {
        UnitEnum.CELSIUS -> value.add(BigDecimal("273.15"))
        UnitEnum.FAHRENHEIT -> (value.subtract(BigDecimal("32"))).multiply(BigDecimal("5")).divide(BigDecimal("9"), scale + 5, RoundingMode.HALF_UP).add(BigDecimal("273.15")) // Increased precision for intermediate step
        UnitEnum.KELVIN -> value
        else -> throw IllegalArgumentException("Unsupported temperature unit for conversion: ${fromUnit.name}")
    }

    // Convert from Kelvin to target unit
    return when (toUnit) {
        UnitEnum.CELSIUS -> kelvinValue.subtract(BigDecimal("273.15")).setScale(scale, RoundingMode.HALF_UP)
        UnitEnum.FAHRENHEIT -> (kelvinValue.subtract(BigDecimal("273.15"))).multiply(BigDecimal("9")).divide(BigDecimal("5"), scale + 5, RoundingMode.HALF_UP).add(BigDecimal("32")).setScale(scale, RoundingMode.HALF_UP) // Increased precision
        UnitEnum.KELVIN -> kelvinValue.setScale(scale, RoundingMode.HALF_UP)
        else -> throw IllegalArgumentException("Unsupported temperature unit for conversion: ${toUnit.name}")
    }
}


/**
 * Parses a unit string (e.g., "h", "mins", "km") and returns the corresponding Unit enum.
 */
fun parseUnit(unitString: String): UnitEnum? {
    return when (unitString.lowercase()) {
        // Time
        "h", "hr", "hour", "hours" -> UnitEnum.HOUR
        "min", "mins", "minute", "minutes" -> UnitEnum.MINUTE
        "s", "sec", "secs", "second", "seconds" -> UnitEnum.SECOND
        "ms", "milli", "millisecond", "milliseconds" -> UnitEnum.MILLISECOND

        // Length
        "km", "kilometer", "kilometers" -> UnitEnum.KILOMETER
        "m", "meter", "meters" -> UnitEnum.METER
        "cm", "centimeter", "centimeters" -> UnitEnum.CENTIMETER
        "mm", "millimeter", "millimeters" -> UnitEnum.MILLIMETER
        "mi", "mile", "miles" -> UnitEnum.MILE
        "yd", "yard", "yards" -> UnitEnum.YARD
        "ft", "foot", "feet" -> UnitEnum.FOOT
        "in", "inch", "inches" -> UnitEnum.INCH

        // Mass
        "kg", "kilogram", "kilograms" -> UnitEnum.KILOGRAM
        "g", "gram", "grams" -> UnitEnum.GRAM
        "lb", "lbs", "pound", "pounds" -> UnitEnum.POUND
        "oz", "ounce", "ounces" -> UnitEnum.OUNCE
        "ton", "tonne", "tonnes" -> UnitEnum.TONNE // Metric tonne

        // Volume
        "l", "liter", "liters" -> UnitEnum.LITER
        "ml", "milliliter", "milliliters" -> UnitEnum.MILLILITER
        "m3", "cubicmeter", "cubicmeters" -> UnitEnum.CUBIC_METER
        "gal", "gallon", "gallons" -> UnitEnum.GALLON_US

        // Temperature
        "c", "celsius" -> UnitEnum.CELSIUS
        "f", "fahrenheit" -> UnitEnum.FAHRENHEIT
        "k", "kelvin" -> UnitEnum.KELVIN

        else -> null
    }
}

/**
 * A utility function to parse the input query string.
 * Expects a format like "10km to miles" or "1 h to mins".
 */
fun parseConversionQuery(query: String): Triple<BigDecimal, UnitEnum, UnitEnum>? {
    val trimmedQuery = query.trim()
    val parts = trimmedQuery.split(" ").filter { it.isNotBlank() }

    val toIndex = parts.indexOfFirst { it.lowercase() == "to" }

    if (toIndex == -1 || toIndex < 1 || toIndex >= parts.size - 1) {
        return null
    }

    val fromPartString = parts.subList(0, toIndex).joinToString("")
    val valueMatch = Regex("([\\d.]+)\\s*([a-zA-Z]+)").find(fromPartString)
    val value: BigDecimal?
    val fromUnitString: String?
    val fromUnit: UnitEnum?

    if (valueMatch != null) {
        value = valueMatch.groups[1]?.value?.toBigDecimalOrNull()
        fromUnitString = valueMatch.groups[2]?.value
        fromUnit = fromUnitString?.let { parseUnit(it) }
    } else {
        if (parts.size >= 3 && toIndex == 2) {
            value = parts[0].toBigDecimalOrNull()
            fromUnitString = parts[1]
            fromUnit = parseUnit(fromUnitString)
        } else {
            return null
        }
    }

    val toUnitString = parts.subList(toIndex + 1, parts.size).joinToString(" ")
    val toUnit = parseUnit(toUnitString)

    if (value == null) {
        return null
    }
    if (fromUnit == null) {
        return null
    }
    if (toUnit == null) {
        return null
    }
    if (fromUnit == toUnit) {
        return null
    }

    return Triple(value, fromUnit, toUnit)
}

private fun getUnitConversionLabel(details: ConversionDetails): String {
    val convertedValue = if (details.convertedValue.toDouble() % 1.0 == 0.0) {
        details.convertedValue.toInt().toString()
    } else {
        details.convertedValue.toPlainString()
    }

    return "${details.originalValue.toPlainString()} ${details.fromUnit.displayName()} is equal to $convertedValue ${details.toUnit.displayName()}"
}

data class ConversionDetails(
    val originalValue: BigDecimal,
    val fromUnit: UnitEnum,
    val toUnit: UnitEnum,
    val convertedValue: BigDecimal,
    val originalQuery: String
)

data class UnitConversionState(
    val onUnitPress: () -> Unit,
    val conversionDetails: ConversionDetails?
)

@Composable
fun getUnitConversionState(launchpad: LaunchpadState): UnitConversionState {
    val currentQuery = launchpad.searchText.text

    val details: ConversionDetails? = remember(currentQuery) {
        if (currentQuery.isNotEmpty() && launchpad.settings.isUnitConversionEnabled) {
            parseConversionQuery(currentQuery)?.let { (value, fromUnit, toUnit) ->
                convertUnits(value, fromUnit, toUnit)?.let { converted ->
                    ConversionDetails(
                        originalValue = value,
                        fromUnit = fromUnit,
                        toUnit = toUnit,
                        convertedValue = converted,
                        originalQuery = currentQuery
                    )
                }
            }
        } else {
            null
        }
    }


    fun onUnitPressCallback() {
        if (details == null) {
            return
        }

        val convertedValue = if (details.convertedValue.toDouble() % 1.0 == 0.0) {
            details.convertedValue.toInt().toString()
        } else {
            details.convertedValue.toPlainString()
        }
        val copyContent = "$convertedValue ${details.toUnit.displayName()}"

        launchpad.saveLastAction(HistoryItem(
            type = "unitConversion",
            label = getUnitConversionLabel(details),
            actionValue = copyContent
        ))
        launchpad.copyToClipboard(
            "Conversion result:",
            copyContent
        )
    }

    return UnitConversionState(
        onUnitPress = ::onUnitPressCallback,
        conversionDetails = details
    )
}

@Composable
fun UnitConversionView(unitConversionState: UnitConversionState) {
    val details = unitConversionState.conversionDetails

    LaunchpadRowItem(
        icon = null,
        label = if (details != null) {
            getUnitConversionLabel(details)
        } else {
            ""
        },
        subLabel = null,
        onClick = { unitConversionState.onUnitPress() }
    )
}