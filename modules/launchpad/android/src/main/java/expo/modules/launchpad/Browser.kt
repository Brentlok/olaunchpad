package expo.modules.launchpad

import android.net.Uri
import androidx.compose.runtime.*

data class BrowserState(
    val onBrowserPress: (query: String) -> Unit,
    val openQueryInBrowser: (query: String) -> Unit
)

@Composable
fun getBrowserState(launchpad: LaunchpadState): BrowserState {
    fun openQueryInBrowser(query: String) {
        openUrl(launchpad.context, "https://unduck.link?q=${Uri.encode(query)}")
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
        openQueryInBrowser = ::openQueryInBrowser
    )
}


@Composable
fun BrowserView(query: String, browserState: BrowserState) {
    LaunchpadRowItem(
        icon = null,
        label = "Search in browser '$query'",
        subLabel = null,
        onClick = { browserState.onBrowserPress(query) }
    )
}