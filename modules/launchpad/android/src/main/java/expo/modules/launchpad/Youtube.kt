package expo.modules.launchpad

import android.app.SearchManager
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.ImageBitmap

data class YoutubeState(
    val onYoutubePress: (query: String) -> Unit,
    val openQueryInYoutube: (query: String) -> Unit,
    val youtubeIcon: ImageBitmap?
)

@Composable
fun getYoutubeState(launchpad: LaunchpadState): YoutubeState {
    val youtubeIcon = remember {
        val iconDrawable = getAppIconFromPackageName(launchpad.context, "com.google.android.youtube") ?: getAppIconFromPackageName(launchpad.context, launchpad.settings.defaultBrowser ?: "")

        if (iconDrawable != null) {
            drawableToImageBitmap(iconDrawable)
        } else {
            null
        }
    }

    fun openQueryInYoutube(query: String) {
        if (launchpad.settings.youtubeSearchInBrowser) {
            openUrl(launchpad.context, launchpad.settings.defaultBrowser, "https://m.youtube.com/results?search_query=${Uri.encode(query)}")
        } else {
            val intent = Intent(Intent.ACTION_SEARCH).apply {
                setPackage("com.google.android.youtube")
                putExtra(SearchManager.QUERY, query)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            launchpad.context.startActivity(intent)
        }

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
        openQueryInYoutube = ::openQueryInYoutube,
        youtubeIcon = youtubeIcon
    )
}

@Composable
fun YoutubeView(query: String, youtubeState: YoutubeState) {
    LaunchpadRowItem(
        icon = youtubeState.youtubeIcon,
        label = "Search in Youtube '$query'",
        subLabel = null,
        onClick = { youtubeState.onYoutubePress(query) }
    )
}