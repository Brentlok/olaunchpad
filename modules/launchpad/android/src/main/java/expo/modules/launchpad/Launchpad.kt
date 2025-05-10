package expo.modules.launchpad

import android.content.Intent
import android.content.pm.ResolveInfo
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
import com.tencent.mmkv.MMKV

@Composable
fun Launchpad(closeLaunchpad: () -> Unit) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val packageManager = context.packageManager
    val mmkv = remember { MMKV.mmkvWithID("mmkv.default", MMKV.MULTI_PROCESS_MODE) }

    var settings by remember {
        mutableStateOf(getSettings(mmkv))
    }

    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val apps = remember(searchText.text) {
        if (searchText.text.isNotEmpty() && settings.isApplicationsEnabled) getInstalledApps(context, searchText.text) else emptyList()
    }
    val contacts = remember(searchText.text) {
        if (searchText.text.isNotEmpty() && settings.isContactsEnabled) getContacts(context, searchText.text) else emptyList()
    }

    fun onOpenURL(url: String) {
        openUrl(context, url)
        closeLaunchpad()
    }

    fun onAppClick(appInfo: ResolveInfo) {
        val launchIntent = packageManager.getLaunchIntentForPackage(appInfo.activityInfo.packageName)
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
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
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(
                            color = colorResource(id = R.color.dark),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .focusRequester(focusRequester),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(id = R.color.white),
                        unfocusedTextColor = colorResource(id = R.color.white),
                        focusedBorderColor = colorResource(id = R.color.primary),
                        unfocusedBorderColor = Color.Transparent,
                        unfocusedPlaceholderColor = colorResource(id = R.color.white_50),
                        focusedPlaceholderColor = colorResource(id = R.color.white_50),
                    )
                )
                if (searchText.text.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
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
                        if (settings.isPlayStoreEnabled && apps.size == 0) {
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
                            items(apps) { app ->
                                val label = app.loadLabel(packageManager).toString()
                                val iconDrawable = app.loadIcon(packageManager)
                                val iconBitmap = remember(app) { drawableToImageBitmap(iconDrawable) }

                                LaunchpadRowItem(
                                    icon = iconBitmap,
                                    label = label,
                                    onClick = { onAppClick(app) },
                                    subLabel = null
                                )
                            }
                        }
                        if (settings.isContactsEnabled) {
                            items(contacts) { contact ->
                                val imageBitmap = remember(contact) {
                                    contact.photoUri?.let { uri ->
                                        uriToImageBitmap(context, uri)
                                    }
                                }

                                LaunchpadRowItem(
                                    icon = imageBitmap,
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
