package expo.modules.launchpad

import androidx.compose.runtime.*
import java.util.Stack

data class CalculatorState(
    val onCalculatorPress: (query: String) -> Unit,
    val calculation: String?
)

private fun evaluateExpression(expression: String): Double? {
    // Function to determine operator precedence
    fun precedence(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/' -> 2
            '^' -> 3
            else -> -1
        }
    }

    // Function to perform an operation
    fun applyOperation(op: Char, b: Double, a: Double): Double? {
        return when (op) {
            '+' -> a + b
            '-' -> a - b
            '*' -> a * b
            '/' -> if (b != 0.0) a / b else null // Handle division by zero
            '^' -> Math.pow(a, b)
            else -> null
        }
    }

    // Convert the infix expression to postfix using the Shunting Yard algorithm
    fun infixToPostfix(expression: String): List<String>? {
        val output = mutableListOf<String>()
        val operators = Stack<Char>()
        val tokens = expression.replace("\\s+".toRegex(), "").toCharArray()

        var i = 0
        while (i < tokens.size) {
            val token = tokens[i]

            when {
                // If the token is a number, add it to the output
                token.isDigit() || token == '.' -> {
                    val number = StringBuilder()
                    while (i < tokens.size && (tokens[i].isDigit() || tokens[i] == '.')) {
                        number.append(tokens[i])
                        i++
                    }
                    output.add(number.toString())
                    continue
                }
                // If the token is an operator, pop operators with higher or equal precedence
                "+-*/^".contains(token) -> {
                    while (operators.isNotEmpty() && precedence(operators.peek()) >= precedence(token)) {
                        output.add(operators.pop().toString())
                    }
                    operators.push(token)
                }
                // If the token is a left parenthesis, push it onto the stack
                token == '(' -> operators.push(token)
                // If the token is a right parenthesis, pop until a left parenthesis is found
                token == ')' -> {
                    while (operators.isNotEmpty() && operators.peek() != '(') {
                        output.add(operators.pop().toString())
                    }
                    if (operators.isEmpty() || operators.peek() != '(') {
                        return null // Mismatched parentheses
                    }
                    operators.pop()
                }
                else -> return null // Invalid character
            }
            i++
        }

        // Pop any remaining operators
        while (operators.isNotEmpty()) {
            if (operators.peek() == '(' || operators.peek() == ')') {
                return null // Mismatched parentheses
            }
            output.add(operators.pop().toString())
        }

        return output
    }

    // Evaluate the postfix expression
    fun evaluatePostfix(postfix: List<String>?): Double? {
        if (postfix == null) return null
        val stack = Stack<Double>()

        for (token in postfix) {
            when {
                // If the token is a number, push it onto the stack
                token.toDoubleOrNull() != null -> stack.push(token.toDouble())
                // If the token is an operator, pop two values and apply the operation
                "+-*/^".contains(token) -> {
                    if (stack.size < 2) return null // Not enough operands
                    val b = stack.pop()
                    val a = stack.pop()
                    val result = applyOperation(token[0], b, a) ?: return null
                    stack.push(result)
                }
                else -> return null // Invalid token
            }
        }

        return if (stack.size == 1) stack.pop() else null // Ensure only one result remains
    }

    // Convert the expression to postfix and evaluate it
    val postfix = infixToPostfix(expression)
    val result = evaluatePostfix(postfix)

    if (result != null) {
        val resultString = if (result % 1.0 == 0.0) {
            result.toInt().toString()
        } else {
            result.toString()
        }

        return if (resultString == expression) {
            null
        } else {
            result
        }
    } else {
        return null
    }
}

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