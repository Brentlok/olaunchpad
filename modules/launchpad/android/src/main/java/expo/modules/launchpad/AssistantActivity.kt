package expo.modules.launchpad

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import com.tencent.mmkv.MMKV

class AssistantActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MMKV.initialize(this)

        val isBlurEnabled = MMKV.mmkvWithID("mmkv.default", MMKV.MULTI_PROCESS_MODE).decodeString("isBlurEnabled") == "true"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && isBlurEnabled) {
            window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            window.attributes.blurBehindRadius = 10
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.post {
                window.insetsController?.let { controller ->
                    controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                    controller.systemBarsBehavior =
                        WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                }
            }
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }

        setContent {
            val mmkv = MMKV.mmkvWithID("mmkv.default", MMKV.MULTI_PROCESS_MODE)
            val appColors = AppColors(
                accentColor = parseHexColor(mmkv.decodeString("accentColor")?.removeSurrounding("\"")),
                textColor = parseHexColor(mmkv.decodeString("textColor")?.removeSurrounding("\""))
            )

            CompositionLocalProvider(LocalAppColors provides appColors) {
                Launchpad(closeLaunchpad = { finish() })
            }
        }
    }
}

