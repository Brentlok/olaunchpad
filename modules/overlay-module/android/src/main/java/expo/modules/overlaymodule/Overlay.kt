package expo.modules.overlaymodule

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import android.content.pm.ResolveInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items

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
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { /* Consume click, do nothing */ }
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = onSearchTextChange,
                    placeholder = { Text("Search...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .focusRequester(focusRequester)
                )
                if (searchText.text.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    val context = LocalContext.current
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 320.dp) // Adjust as needed
                    ) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpenURL("https://unduck.link?q=${Uri.encode(searchText.text)}") }
                                    .padding(8.dp)
                            ) {
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = "Search: ${searchText.text}")
                            }
                        }
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onOpenURL("https://www.youtube.com/results?search_query=${Uri.encode(searchText.text)}") }
                                    .padding(8.dp)
                            ) {
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = "Search in YouTube: ${searchText.text}")
                            }
                        }
                        items(apps) { app ->
                            val label = app.loadLabel(context.packageManager).toString()
                            val iconDrawable = app.loadIcon(context.packageManager)
                            val iconBitmap = remember(app) { drawableToImageBitmap(iconDrawable) }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onAppClick(app) }
                                    .padding(8.dp)
                            ) {
                                Image(
                                    bitmap = iconBitmap,
                                    contentDescription = label,
                                    modifier = Modifier.size(32.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = label)
                            }
                        }
                    }
                }
            }
        }
    }
}