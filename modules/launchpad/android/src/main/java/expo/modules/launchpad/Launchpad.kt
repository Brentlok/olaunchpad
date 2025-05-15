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
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.delay

@Composable
fun Launchpad(closeLaunchpad: () -> Unit) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val packageManager = context.packageManager
    val mmkv = remember { MMKV.mmkvWithID("mmkv.default", MMKV.MULTI_PROCESS_MODE) }
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    var settings by remember {
        mutableStateOf(getSettings(mmkv))
    }

    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    var apps by remember { mutableStateOf<List<InstalledApp>>(emptyList()) }
    var contacts by remember { mutableStateOf<List<Contact>>(emptyList()) }
    val filteredApps = remember(searchText.text) {
        filterAndTake(
            list = apps,
            callback =  { it.label.contains(searchText.text, ignoreCase = true) },
            count = 3
        )
    }
    val filteredContacts = remember(searchText.text) {
        filterAndTake(
            list = contacts,
            callback =  { it.label.contains(searchText.text, ignoreCase = true) || it.phoneNumber.toString().contains(searchText.text, ignoreCase = true) },
            count = 3
        )
    }
    val calculation = remember(searchText.text) {
        if (searchText.text.isNotEmpty() && settings.isCalculatorEnabled) evaluateExpression(searchText.text) else null
    }

    fun onOpenURL(url: String) {
        openUrl(context, url)
        closeLaunchpad()
    }

    fun onAppClick(installedApp: InstalledApp) {
        val launchIntent = packageManager.getLaunchIntentForPackage(installedApp.packageName)
        context.startActivity(launchIntent)
        closeLaunchpad()
    }

    fun onPhoneClick(phone: String?) {
        if (phone != null) {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = "tel:$phone".toUri()
            }
            context.startActivity(intent)
        }

        closeLaunchpad()
    }

    fun onPlayStoreSearch() {
        val query = Uri.encode(searchText.text)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = "market://search?q=$query".toUri()
            setPackage("com.android.vending")
        }
        val playStoreAvailable = intent.resolveActivity(context.packageManager) != null

        if (playStoreAvailable) {
            context.startActivity(intent)
        } else {
            val webIntent = Intent(Intent.ACTION_VIEW).apply {
                data = "https://play.google.com/store/search?q=$query".toUri()
            }
            context.startActivity(webIntent)
        }

        closeLaunchpad()
    }

    fun copyToClipboard(label: String, text: String) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
        closeLaunchpad()
    }


    LaunchedEffect(settings) {
        delay(500)
        if (settings.isApplicationsEnabled) {
            apps = getInstalledApps(context)
        }
        if (settings.isContactsEnabled) {
            contacts = getContacts(context)
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
                                onOpenURL("https://unduck.link?q=${Uri.encode(searchText.text)}")
                            }
                        }
                    )
                )
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
                                LaunchpadRowItem(
                                    icon = null,
                                    label = "Search in browser for '${searchText.text}'",
                                    subLabel = null,
                                    onClick = { onOpenURL("https://unduck.link?q=${Uri.encode(searchText.text)}")  }
                                )
                            }
                        }
                        if (settings.isYoutubeEnabled) {
                            item {
                                LaunchpadRowItem(
                                    icon = null,
                                    label = "Search in YouTube for '${searchText.text}'",
                                    subLabel = null,
                                    onClick = { onOpenURL("https://www.youtube.com/results?search_query=${Uri.encode(searchText.text)}")  }
                                )
                            }
                        }
                        if (settings.isPlayStoreEnabled && filteredApps.isEmpty()) {
                            item {
                                LaunchpadRowItem(
                                    icon = null,
                                    label = "Search in Play Store for '${searchText.text}'",
                                    onClick = { onPlayStoreSearch() },
                                    subLabel = null
                                )
                            }
                        }
                        if (settings.isApplicationsEnabled) {
                            items(filteredApps) { app ->
                                LaunchpadRowItem(
                                    icon = app.icon,
                                    label = app.label,
                                    onClick = { onAppClick(app) },
                                    subLabel = null
                                )
                            }
                        }
                        if (settings.isContactsEnabled) {
                            items(filteredContacts) { contact ->
                                LaunchpadRowItem(
                                    icon = contact.icon,
                                    label = contact.label,
                                    onClick = { onPhoneClick(contact.phoneNumber) },
                                    subLabel = contact.phoneNumber
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
