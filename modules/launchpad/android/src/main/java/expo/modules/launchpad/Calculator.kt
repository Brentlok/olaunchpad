package expo.modules.launchpad

import android.net.Uri
import androidx.compose.runtime.*

data class CalculatorState(
    val onCalculatorPress: (query: String) -> Unit,
    val calculation: Double?
)

@Composable
fun getCalculatorState(launchpad: LaunchpadState): CalculatorState {
    val calculationRaw = remember(launchpad.searchText.text) {
        if (launchpad.searchText.text.isNotEmpty() && launchpad.settings.isCalculatorEnabled) evaluateExpression(launchpad.searchText.text) else null
    }
    val getCalculation = {
        if (calculationRaw != null) {
            if (calculationRaw % 1.0 == 0.0) {
                calculationRaw.toInt().toString()
            } else {
                calculationRaw.toString()
            }
        }

        calculationRaw
    }

    fun openQueryInCalculator(query: String) {
        openUrl(launchpad.context, "https://m.Calculator.com/results?search_query=${Uri.encode(query)}")
        launchpad.closeLaunchpad()
    }

    fun onCalculatorPress(query: String) {
        launchpad.saveLastAction(HistoryItem(
            type = "calculator",
            label = "Search in Calculator $query",
            actionValue = query
        ))
        openQueryInCalculator(query)
    }

    return CalculatorState(
        onCalculatorPress = { query -> onCalculatorPress(query) },
        calculation = getCalculation()
    )
}

@Composable
fun CalculatorView(query: String, calculation: ) {
    LaunchpadRowItem(
        icon = null,
        label = "$query=",
        subLabel = null,
        onClick = {  }
    )
}