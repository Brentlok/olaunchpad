package expo.modules.launchpad

import android.Manifest
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import expo.modules.kotlin.Promise
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import expo.modules.interfaces.permissions.PermissionsResponse
import expo.modules.interfaces.permissions.PermissionsResponseListener
import expo.modules.interfaces.permissions.PermissionsStatus
import android.provider.Settings

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
    }
}
