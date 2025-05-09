package expo.modules.overlaymodule

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
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import android.content.pm.ResolveInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource

@Composable
fun OverlayWithButtons(
    searchText: TextFieldValue,
    onOpenURL: (String) -> Unit,
    onDismiss: () -> Unit,
    apps: List<ResolveInfo>,
    onAppClick: (ResolveInfo) -> Unit,
    onSearchTextChange: (TextFieldValue) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
        keyboardController?.show()
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.25f))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onDismiss() },
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
                    onValueChange = onSearchTextChange,
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
                    val context = LocalContext.current
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            OverlayRowItem(
                                icon = null,
                                label = "Search in browser for '${searchText.text}'",
                                onClick = { onOpenURL("https://unduck.link?q=${Uri.encode(searchText.text)}")  }
                            )
                        }
                        item {
                            OverlayRowItem(
                                icon = null,
                                label = "Search in YouTube for '${searchText.text}'",
                                onClick = { onOpenURL("https://www.youtube.com/results?search_query=${Uri.encode(searchText.text)}")  }
                            )
                        }
                        items(apps) { app ->
                            val label = app.loadLabel(context.packageManager).toString()
                            val iconDrawable = app.loadIcon(context.packageManager)
                            val iconBitmap = remember(app) { drawableToImageBitmap(iconDrawable) }

                            OverlayRowItem(
                                icon = iconBitmap,
                                label = label,
                                onClick = { onAppClick(app) }
                            )
                        }
                    }
                }
            }
        }
    }
}