package expo.modules.overlaymodule

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

class AssistantActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            window.attributes.blurBehindRadius = 10
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        setContent {
            val context = LocalContext.current
            var searchText by remember { mutableStateOf(TextFieldValue("")) }
            val apps = remember(searchText.text) {
                if (searchText.text.isNotEmpty()) getInstalledApps(context, searchText.text) else emptyList()
            }
            OverlayWithButtons(
                searchText = searchText,
                onOpenURL = { url ->
                    openUrl(context, url)
                    finish()
                },
                onDismiss = { finish() },
                apps = apps,
                onAppClick = { appInfo ->
                    val launchIntent = context.packageManager.getLaunchIntentForPackage(appInfo.activityInfo.packageName)
                    context.startActivity(launchIntent)
                    finish()
                },
                onSearchTextChange = { searchText = it }
            )
        }
    }
}

