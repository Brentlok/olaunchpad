package expo.modules.launchpad

import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap

data class BrowserState(
    val onBrowserPress: (query: String) -> Unit,
    val openQueryInBrowser: (query: String) -> Unit,
    val browserIcon: ImageBitmap?
)

@Composable
fun getBrowserState(launchpad: LaunchpadState): BrowserState {
    val browserIcon = remember {
        val iconDrawable = getAppIconFromPackageName(launchpad.context, launchpad.settings.defaultBrowser ?: "")

        if (iconDrawable != null) {
            drawableToImageBitmap(iconDrawable)
        } else {
            null
        }
    }

    fun openQueryInBrowser(query: String) {
        openUrl(launchpad.context, launchpad.settings.defaultBrowser, "https://unduck.link?q=${Uri.encode(query)}")
        launchpad.closeLaunchpad()
    }

    fun onBrowserPress(query: String) {
        launchpad.saveLastAction(HistoryItem(
            type = "browser",
            label = "Search in browser $query",
            actionValue = query
        ))
        openQueryInBrowser(query)
    }

    return BrowserState(
        onBrowserPress = ::onBrowserPress,
        openQueryInBrowser = ::openQueryInBrowser,
        browserIcon = browserIcon
    )
}


@Composable
fun BrowserView(query: String, browserState: BrowserState) {
    LaunchpadRowItem(
        icon = browserState.browserIcon,
        label = "Search in browser '$query'",
        subLabel = null,
        onClick = { browserState.onBrowserPress(query) }
    )
}