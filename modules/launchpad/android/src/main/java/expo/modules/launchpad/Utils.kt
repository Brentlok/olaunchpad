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
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

suspend fun getInstalledApps(context: Context): List<InstalledApp> =
    withContext(Dispatchers.IO) {
        val pm = context.packageManager
        val intent = Intent(Intent.ACTION_MAIN, null)
        intent.addCategory(Intent.CATEGORY_LAUNCHER)
        val appsInfo = pm.queryIntentActivities(intent, 0)

        appsInfo.mapNotNull { resolveInfo ->
            try {
                val packageName = resolveInfo.activityInfo.packageName
                val label = resolveInfo.loadLabel(pm).toString()

                if (packageName.isNotEmpty() && label.isNotEmpty()) {
                    InstalledApp(
                        label = label,
                        packageName = packageName,
                        resolveInfo = resolveInfo
                    )
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            }
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

suspend fun getContacts(context: Context): List<Contact> {
    val permission = Manifest.permission.READ_CONTACTS
    val granted =
        context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED

    if (!granted) {
        return emptyList()
    }

    return withContext(Dispatchers.IO) {
        val contactsList = mutableListOf<Contact>()

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_URI
        )

        val cursor: Cursor? = context.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        cursor?.use {
            val nameIndex =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
            val numberIndex =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
            val photoIndex =
                it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)

            if (nameIndex == -1 || numberIndex == -1 || photoIndex == -1) {
                return@withContext emptyList<Contact>()
            }

            while (it.moveToNext()) {
                val name = it.getString(nameIndex)
                val phoneNumber = it.getString(numberIndex)
                val photoUriString = it.getString(photoIndex)
                val photoUri = photoUriString?.toUri()

                val photoBitmap = if (photoUri != null) {
                    uriToImageBitmap(context, photoUri)
                } else {
                    null
                }
                contactsList.add(Contact(name, phoneNumber, photoBitmap))
            }
        }
        contactsList
    }
}

fun uriToImageBitmap(context: Context, uri: Uri?): ImageBitmap? {
    if (uri == null) {
        return null
    }

    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source).asImageBitmap()
        } else {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)?.asImageBitmap()
            }
        }
    } catch (e: Exception) {
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
       isUnitConversionEnabled = mmkv.decodeString("isUnitConversionEnabled") == "true",
       youtubeSearchInBrowser = mmkv.decodeString("youtubeSearchInBrowser") == "true",
       defaultBrowser = mmkv.decodeString("defaultBrowser")?.removeSurrounding("\"")
   )
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

fun parseHexColor(hexColor: String?): Color {
    return try {
        val cleanHex = hexColor?.removePrefix("#") ?: "000000"
        val colorLong = cleanHex.toLong(16) or 0xFF000000L

        Color(colorLong)
    } catch (e: IllegalArgumentException) {
        Color.Black
    }
}

fun getAppIconFromPackageName(context: Context, packageName: String): Drawable? {
    return try {
        val packageManager: PackageManager = context.packageManager
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        packageManager.getApplicationIcon(applicationInfo)
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}
