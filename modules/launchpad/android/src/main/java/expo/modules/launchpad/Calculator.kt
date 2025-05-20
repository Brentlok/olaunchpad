package expo.modules.launchpad

import androidx.compose.runtime.*

data class CalculatorState(
    val onCalculatorPress: (query: String) -> Unit,
    val calculation: String?
)

@Composable
fun getCalculatorState(launchpad: LaunchpadState): CalculatorState {
    val calculationRaw = remember(launchpad.searchText.text) {
        if (launchpad.searchText.text.isNotEmpty() && launchpad.settings.isCalculatorEnabled) evaluateExpression(launchpad.searchText.text) else null
    }

    val calculation = remember(calculationRaw) {
        if (calculationRaw != null) {
            if (calculationRaw % 1.0 == 0.0) {
                calculationRaw.toInt().toString()
            } else {
                calculationRaw.toString()
            }
        } else {
            null
        }
    }

    fun onCalculatorPress(query: String) {
        launchpad.saveLastAction(HistoryItem(
            type = "calculator",
            label = "$query=$calculation",
            actionValue = calculation ?: ""
        ))
        launchpad.copyToClipboard("Calculation result:", calculation ?: "")
    }

    return CalculatorState(
        onCalculatorPress = ::onCalculatorPress,
        calculation = calculation
    )
}

@Composable
fun CalculatorView(query: String, calculatorState: CalculatorState) {
    LaunchpadRowItem(
        icon = null,
        label = "$query=${calculatorState.calculation}",
        subLabel = null,
        onClick = { calculatorState.onCalculatorPress(query) }
    )
}