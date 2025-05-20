package expo.modules.launchpad

import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextOverflow

data class HistoryItem(
    val type: String,
    val label: String,
    val actionValue: String
)

data class HistoryState(
    val onHistoryClick: (historyItem: HistoryItem) -> Unit,
)

@Composable
fun getHistoryState(
    launchpadState: LaunchpadState,
    contactsState: ContactsState,
    installedAppsState: InstalledAppsState,
    browserState: BrowserState,
    youtubeState: YoutubeState,
    playStoreState: PlayStoreState
): HistoryState {
    return HistoryState(
        onHistoryClick = { historyItem ->
            launchpadState.reorderHistoryList(historyItem)
            when (historyItem.type) {
                "phone" -> contactsState.callPhone(historyItem.actionValue)
                "browser" -> browserState.openQueryInBrowser(historyItem.actionValue)
                "calculator" -> launchpadState.copyToClipboard("Calculation result:", historyItem.actionValue)
                "app" -> installedAppsState.openApp(historyItem.actionValue)
                "youtube" -> youtubeState.openQueryInYoutube(historyItem.actionValue)
                "playStore" -> playStoreState.openQueryInPlayStore(historyItem.actionValue)
            }
        }
    )
}

@Composable
fun HistoryView(historyItem: HistoryItem, historyState: HistoryState) {
    AssistChip(
        onClick = { historyState.onHistoryClick(historyItem) },
        label = {
            Text(
                text = historyItem.label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = colorResource(id = R.color.gray),
            labelColor = colorResource(id = R.color.white)
        )
    )
}