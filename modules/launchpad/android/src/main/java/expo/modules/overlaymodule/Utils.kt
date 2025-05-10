package expo.modules.launchpad

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.ContactsContract
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri

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

fun getInstalledApps(context: Context, query: String): List<ResolveInfo> {
    val pm = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val apps = pm.queryIntentActivities(intent, 0)
    return apps.filter {
        val label = it.loadLabel(pm).toString()
        label.contains(query, ignoreCase = true)
    }.take(3)
}

fun openUrl(context: Context, url: String) {
    val pm: PackageManager = context.packageManager
    val kiwiPackage = "com.kiwibrowser.browser"
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    val isKiwiInstalled = try {
        pm.getPackageInfo(kiwiPackage, 0)
        true
    } catch (e: Exception) {
        false
    }

    if (isKiwiInstalled) {
        intent.setPackage(kiwiPackage)
    }

    context.startActivity(intent)
}

data class Contact(
    val label: String,
    val phoneNumber: String?,
    val photoUri: Uri?
)

fun getContacts(context: Context, query: String): List<Contact> {
    val permission = Manifest.permission.READ_CONTACTS
    val granted = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    if (!granted) {
        return emptyList()
    }

    val contacts = mutableListOf<Contact>()
    val contentResolver = context.contentResolver

    val selection = "${ContactsContract.Contacts.DISPLAY_NAME} LIKE ?"
    val selectionArgs = arrayOf("%$query%")

    val projection = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME,
        ContactsContract.Contacts.PHOTO_URI
    )

    val cursor = contentResolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        "${ContactsContract.Contacts.DISPLAY_NAME} ASC"
    )

    cursor?.use {
        val idIndex = it.getColumnIndex(ContactsContract.Contacts._ID)
        val nameIndex = it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)
        val photoIndex = it.getColumnIndex(ContactsContract.Contacts.PHOTO_URI)

        while (it.moveToNext() && contacts.size < 3) {
            val contactId = it.getString(idIndex)
            val name = it.getString(nameIndex)
            val photoUri = it.getString(photoIndex)?.toUri()

            // Fetch phone number
            var phone: String? = null
            val phoneCursor = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                arrayOf(contactId),
                null
            )
            phoneCursor?.use { pc ->
                val phoneIndex =
                    pc.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                if (phoneIndex != -1 && pc.moveToFirst()) {
                    phone = pc.getString(phoneIndex)
                }
            }

            contacts.add(Contact(name, phone, photoUri))
        }
    }

    return contacts
}

fun uriToImageBitmap(context: Context, uri: Uri): ImageBitmap? {
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
