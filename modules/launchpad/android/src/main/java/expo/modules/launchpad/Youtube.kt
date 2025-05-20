package expo.modules.launchpad

import android.net.Uri
import androidx.compose.runtime.*

data class YoutubeState(
    val onYoutubePress: (query: String) -> Unit,
    val openQueryInYoutube: (query: String) -> Unit
)

@Composable
fun getYoutubeState(launchpad: LaunchpadState): YoutubeState {
    fun openQueryInYoutube(query: String) {
        openUrl(launchpad.context, "https://m.youtube.com/results?search_query=${Uri.encode(query)}")
        launchpad.closeLaunchpad()
    }

    fun onYoutubePress(query: String) {
        launchpad.saveLastAction(HistoryItem(
            type = "youtube",
            label = "Search in YouTube $query",
            actionValue = query
        ))
        openQueryInYoutube(query)
    }

    return YoutubeState(
        onYoutubePress = ::onYoutubePress,
        openQueryInYoutube = ::openQueryInYoutube
    )
}

@Composable
fun YoutubeView(query: String, youtubeState: YoutubeState) {
    LaunchpadRowItem(
        icon = null,
        label = "Search in Youtube '$query'",
        subLabel = null,
        onClick = { youtubeState.onYoutubePress(query) }
    )
}