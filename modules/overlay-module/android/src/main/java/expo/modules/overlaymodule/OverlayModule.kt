package expo.modules.overlaymodule

import android.content.Intent
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class OverlayModule : Module() {
    override fun definition() = ModuleDefinition {
        Name("OverlayModule")

        Function("hello") {
            "Hello from Expo OverlayModule"
        }

        Function("openAssistantSettings") {
            val intent = Intent("android.settings.VOICE_INPUT_SETTINGS")
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val activity = appContext.currentActivity ?: appContext.reactContext
            activity?.startActivity(intent)
        }
    }
}
