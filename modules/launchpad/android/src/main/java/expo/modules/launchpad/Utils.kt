package expo.modules.launchpad

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri
import com.tencent.mmkv.MMKV
import java.util.Stack

fun drawableToImageBitmap(drawable: Drawable): ImageBitmap {
    val bitmap = createBitmap(
        drawable.intrinsicWidth.takeIf { it > 0 } ?: 1,
        drawable.intrinsicHeight.takeIf { it > 0 } ?: 1,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap.asImageBitmap()
}

fun getInstalledApps(context: Context): List<InstalledApp> {
    val pm = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val apps = pm.queryIntentActivities(intent, 0)

    return apps.map {
        InstalledApp(
            label = it.loadLabel(context.packageManager).toString(),
            icon = drawableToImageBitmap(it.loadIcon(context.packageManager)),
            packageName = it.activityInfo.packageName
        )
    }
}

fun openUrl(context: Context, defaultBrowser: String?,  url: String) {
    val pm: PackageManager = context.packageManager
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    val isBrowserInstalled = try {
        pm.getPackageInfo(defaultBrowser ?: "", 0)
        true
    } catch (e: Exception) {
        false
    }

    if (isBrowserInstalled) {
        intent.setPackage(defaultBrowser)
    }

    context.startActivity(intent)
}

fun getContacts(context: Context): List<Contact> {
    val permission = Manifest.permission.READ_CONTACTS
    val granted = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    if (!granted) {
        return emptyList()
    }

    val contactsList = mutableListOf<Contact>()

    // Define the columns to retrieve
    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.PHOTO_URI
    )

    // Query the contacts database
    val cursor: Cursor? = context.contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        projection,
        null,
        null,
        null
    )

    // Process the cursor
    cursor?.use {
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numberIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val photoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)
        while (it.moveToNext()) {
            val name = it.getString(nameIndex)
            val phoneNumber = it.getString(numberIndex)
            val photoUri = it.getString(photoIndex)?.toUri()

            contactsList.add(Contact(name, phoneNumber, uriToImageBitmap(context, photoUri)))
        }
    }

    return contactsList
}

fun uriToImageBitmap(context: Context, uri: Uri?): ImageBitmap? {
    if (uri == null) {
        return null
    }

    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getSettings(mmkv: MMKV): Settings {
   return Settings(
       isBrowserEnabled = mmkv.decodeString("isBrowserEnabled") == "true",
       isYoutubeEnabled = mmkv.decodeString("isYoutubeEnabled") == "true",
       isApplicationsEnabled = mmkv.decodeString("isApplicationsEnabled") == "true",
       isContactsEnabled = mmkv.decodeString("isContactsEnabled") == "true",
       isPlayStoreEnabled = mmkv.decodeString("isPlayStoreEnabled") == "true",
       isCalculatorEnabled = mmkv.decodeString("isCalculatorEnabled") == "true",
       youtubeSearchInBrowser = mmkv.decodeString("youtubeSearchInBrowser") == "true",
       defaultBrowser = mmkv.decodeString("defaultBrowser")?.removeSurrounding("\"")
   )
}

fun evaluateExpression(expression: String): Double? {
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
    return evaluatePostfix(postfix)
}

fun <T> filterAndTake(
    list: List<T>,
    callback: (T) -> Boolean,
    count: Int
): List<T> {
    val result = mutableListOf<T>()
    for (item in list) {
        if (callback(item)) {
            result.add(item)
            if (result.size == count) {
                break
            }
        }
    }
    return result
}
