package expo.modules.launchpad

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.*
import androidx.core.net.toUri

data class PlayStoreState(
    val onPlayStorePress: (query: String) -> Unit,
    val openQueryInPlayStore: (query: String) -> Unit
)

@Composable
fun getPlayStoreState(launchpad: LaunchpadState): PlayStoreState {
    fun openQueryInPlayStore(query: String) {
        val searchQuery = Uri.encode(query)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = "market://search?q=$searchQuery".toUri()
            setPackage("com.android.vending")
        }
        val playStoreAvailable = intent.resolveActivity(launchpad.context.packageManager) != null

        if (playStoreAvailable) {
            launchpad.context.startActivity(intent)
        } else {
            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                data = "https://play.google.com/store/search?q=$searchQuery".toUri()
            }
            launchpad.context.startActivity(webIntent)
        }

        launchpad.closeLaunchpad()
    }

    fun onPlayStorePress(query: String) {
        launchpad.saveLastAction(HistoryItem(
            type = "playStore",
            label = "Search in Play Store $query",
            actionValue = query
        ))
        openQueryInPlayStore(query)
    }

    return PlayStoreState(
        onPlayStorePress = ::onPlayStorePress,
        openQueryInPlayStore = ::openQueryInPlayStore
    )
}

@Composable
fun PlayStoreView(query: String, playStoreState: PlayStoreState) {
    LaunchpadRowItem(
        icon = null,
        label = "Search in Play Store '$query'",
        subLabel = null,
        onClick = { playStoreState.onPlayStorePress(query) }
    )
}