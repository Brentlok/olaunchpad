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
import com.tencent.mmkv.MMKV

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

fun getContacts(context: Context, query: String): List<Contact> {
    val permission = Manifest.permission.READ_CONTACTS
    val granted = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    if (!granted) {
        return emptyList()
    }

    val contacts = mutableListOf<Contact>()
    val contentResolver = context.contentResolver

    val selection = "(${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} LIKE ? OR " +
            "${ContactsContract.CommonDataKinds.Phone.NUMBER} LIKE ?)"
    val selectionArgs = arrayOf("%$query%", "%$query%")

    val projection = arrayOf(
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.PHOTO_URI
    )

    val cursor = contentResolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        projection,
        selection,
        selectionArgs,
        "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
    )

    val seenIds = mutableSetOf<String>()

    cursor?.use {
        val idIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID)
        val nameIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val phoneIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val photoIndex = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

        while (it.moveToNext() && contacts.size < 3) {
            val contactId = it.getString(idIndex)
            if (seenIds.contains(contactId)) continue // Avoid duplicates
            seenIds.add(contactId)

            val name = it.getString(nameIndex)
            val phone = it.getString(phoneIndex)
            val photoUri = it.getString(photoIndex)?.toUri()

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

fun getSettings(mmkv: MMKV): Settings {
   return Settings(
       isBrowserEnabled = mmkv.decodeString("isBrowserEnabled") == "true",
       isYoutubeEnabled = mmkv.decodeString("isYoutubeEnabled") == "true",
       isApplicationsEnabled = mmkv.decodeString("isApplicationsEnabled") == "true",
       isContactsEnabled = mmkv.decodeString("isContactsEnabled") == "true"
   )
}
