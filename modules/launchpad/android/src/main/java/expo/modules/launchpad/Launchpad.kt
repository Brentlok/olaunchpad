package expo.modules.launchpad

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.core.net.toUri
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.delay
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class LaunchpadState(
    val searchText: TextFieldValue,
    val context: Context,
    val closeLaunchpad: () -> Unit,
    val saveLastAction: (historyItem: HistoryItem) -> Unit,
    val settings: Settings
)

@Composable
fun Launchpad(closeLaunchpad: () -> Unit) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val packageManager = context.packageManager
    val mmkv = remember { MMKV.mmkvWithID("mmkv.default", MMKV.MULTI_PROCESS_MODE) }
    val gson = remember { Gson() }
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    var settings by remember {
        mutableStateOf(getSettings(mmkv))
    }

    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    val calculation = remember(searchText.text) {
        if (searchText.text.isNotEmpty() && settings.isCalculatorEnabled) evaluateExpression(searchText.text) else null
    }

    val historyList: MutableList<HistoryItem> = remember {
        val historyJson = mmkv.decodeString("history")
        val listType = object : TypeToken<MutableList<HistoryItem>>() {}.type
        if (historyJson != null) {
            gson.fromJson(historyJson, listType)
        } else {
            mutableListOf()
        }
    }

    fun saveLastAction(historyItem: HistoryItem) {
        historyList.add(historyItem)
        mmkv.encode("history", gson.toJson(historyList))
    }

    val launchpadState = LaunchpadState(
        closeLaunchpad = closeLaunchpad,
        saveLastAction = { historyItem -> saveLastAction(historyItem) },
        context = context,
        searchText = searchText,
        settings = settings
    )

    val contactsState = getContactsState(launchpadState)
    val installedAppsState = getInstalledAppsState(launchpadState)
    val browserState = getBrowserState(launchpadState)
    val youtubeState = getYoutubeState(launchpadState)
    val playStoreState = getPlayStoreState(launchpadState)

    fun copyToClipboard(label: String, text: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
        closeLaunchpad()
    }

    fun onHistoryClick(historyItem: HistoryItem) {

    }

    fun getHistoryLabel(historyItem: HistoryItem): String {
        return "test"
    }

    LaunchedEffect(settings) {
        delay(500)
        if (settings.isApplicationsEnabled) {
            installedAppsState.fetchApps()
        }
        if (settings.isContactsEnabled) {
            contactsState.fetchContacts()
        }
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
        settings = getSettings(mmkv)
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.25f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { closeLaunchpad() },
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.ime)
        ) {
            IconButton(
                onClick = { context.startActivity(packageManager.getLaunchIntentForPackage("com.brentlok.olaunchpad")) },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings Icon",
                    tint = colorResource(id = R.color.white)
                )
            }
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                    .background(
                        color = colorResource(id = R.color.black),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { }
            ) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            color = colorResource(id = R.color.gray),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .focusRequester(focusRequester),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search Icon",
                            tint = colorResource(id = R.color.white_50)
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(id = R.color.white),
                        unfocusedTextColor = colorResource(id = R.color.white),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        cursorColor = colorResource(id = R.color.white),
                        unfocusedPlaceholderColor = colorResource(id = R.color.white_50),
                        focusedPlaceholderColor = colorResource(id = R.color.white_50),
                    ),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            if (searchText.text.isNotEmpty()) {
                                browserState.onBrowserPress(searchText.text)
                            }
                        }
                    )
                )
                if (historyList.isNotEmpty()) {
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(historyList.reversed()) { historyItem ->
                            AssistChip(
                                onClick = { onHistoryClick(historyItem) },
                                label = {
                                    Text(
                                        text = getHistoryLabel(historyItem),
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
                    }
                }
                if (searchText.text.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (settings.isCalculatorEnabled && calculation != null) {
                            val calculationResult = if (calculation % 1.0 == 0.0) {
                                calculation.toInt().toString()
                            } else {
                                calculation.toString()
                            }

                            item {
                                LaunchpadRowItem(
                                    icon = null,
                                    label = "Equals $calculationResult",
                                    subLabel = "(Copy to clipboard)",
                                    onClick = { copyToClipboard("Calculation", calculationResult)  }
                                )
                            }
                        }
                        if (settings.isBrowserEnabled) {
                            item {
                                BrowserView(searchText.text, browserState)
                            }
                        }
                        if (settings.isYoutubeEnabled) {
                            item {
                                YoutubeView(searchText.text, youtubeState)
                            }
                        }
                        if (settings.isPlayStoreEnabled && installedAppsState.installedApps.isEmpty()) {
                            item {
                                PlayStoreView(searchText.text, playStoreState)
                            }
                        }
                        items(installedAppsState.installedApps) { app ->
                            InstalledAppView(app, installedAppsState)
                        }
                        items(contactsState.contacts) { contact ->
                            ContactView(contact, contactsState)
                        }
                    }
                }
            }
        }
    }
}
