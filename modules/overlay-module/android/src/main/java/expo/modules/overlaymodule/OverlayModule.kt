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
    }
}
