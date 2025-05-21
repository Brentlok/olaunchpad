package expo.modules.launchpad

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import expo.modules.kotlin.Promise
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.interfaces.permissions.PermissionsResponse
import expo.modules.interfaces.permissions.PermissionsResponseListener
import expo.modules.interfaces.permissions.PermissionsStatus
import android.provider.Settings
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Converts a Drawable to a Bitmap
 */
private fun drawableToBitmap(drawable: Drawable): Bitmap {
    if (drawable is BitmapDrawable) {
        return drawable.bitmap
    }

    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

class LaunchpadModule : Module() {
    override fun definition() = ModuleDefinition {
        Name("LaunchpadModule")

        Function("open") {
            val intent = Intent(Intent.ACTION_ASSIST)
            intent.setPackage("com.brentlok.olaunchpad");
            val activity = appContext.currentActivity ?: appContext.reactContext
            activity?.startActivity(intent)
        }

        Function("getHasReadContactsPermission") {
            val context = appContext.reactContext ?: return@Function false
            val permission = Manifest.permission.READ_CONTACTS
            val granted = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
            return@Function granted
        }

        AsyncFunction("requestReadContactsPermission") { promise: Promise ->
            val permissionsManager = appContext.permissions
            if (permissionsManager == null) {
                promise.reject(
                    "E_PERMISSIONS_UNAVAILABLE",
                    "Permissions module is not available.",
                    null
                )
                return@AsyncFunction
            }

            permissionsManager.askForPermissions(
                PermissionsResponseListener { results ->
                    val readContactsPermission = Manifest.permission.READ_CONTACTS
                    if (results[readContactsPermission]?.status == PermissionsStatus.GRANTED) {
                        promise.resolve(true)
                    } else {
                        promise.reject(
                            "E_PERMISSIONS_DENIED",
                            "Read contacts permission denied.",
                            null
                        )
                    }
                },
                Manifest.permission.READ_CONTACTS
            )
        }

        Function("getIsDefaultAssistant") {
            val context = appContext.reactContext ?: return@Function false
            val myPackageName = context.packageName
            val assistantSetting = Settings.Secure.getString(context.contentResolver, "assistant")

            if (assistantSetting.isNullOrEmpty()) {
                return@Function false
            }

            val defaultAssistantComponent = ComponentName.unflattenFromString(assistantSetting)

            if (defaultAssistantComponent != null) {
                return@Function defaultAssistantComponent.packageName == myPackageName
            }

            return@Function false
        }

        Function("getDefaultBrowser") {
            val context = appContext.reactContext ?: return@Function null

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://"))
            val packageManager = context.packageManager

            // Resolve the default activity for the intent
            val resolveInfo = packageManager.resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )

            // Return the package name, or null if not found
            return@Function resolveInfo?.activityInfo?.packageName
        }

        Function("getAllBrowsers") {
            val context = appContext.reactContext ?: return@Function emptyList<Nothing>()
            val packageManager = context.packageManager
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://"))
            val resolveInfoList = packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_ALL
            )

            return@Function resolveInfoList.map { resolveInfo ->
                val label = resolveInfo.loadLabel(packageManager).toString()
                val icon = resolveInfo.loadIcon(packageManager)
                val packageName = resolveInfo.activityInfo.packageName

                // Convert icon to base64
                val bitmap = drawableToBitmap(icon)
                val outputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                val base64Icon = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

                mapOf(
                    "label" to label,
                    "icon" to base64Icon,
                    "packageName" to packageName
                )
            }
        }
    }
}
