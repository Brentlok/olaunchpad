package expo.modules.overlaymodule

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import android.content.Context
import android.content.pm.ResolveInfo
import androidx.compose.ui.platform.LocalContext
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size

fun drawableToImageBitmap(drawable: Drawable): ImageBitmap {
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth.takeIf { it > 0 } ?: 1,
        drawable.intrinsicHeight.takeIf { it > 0 } ?: 1,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap.asImageBitmap()
}

fun getInstalledApps(context: Context, query: String): List<ResolveInfo> {
    val pm = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val apps = pm.queryIntentActivities(intent, 0)
    return apps.filter {
        val label = it.loadLabel(pm).toString()
        label.contains(query, ignoreCase = true)
    }.take(5)
}

class AssistantActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            window.attributes.blurBehindRadius = 10
        }

        setContent {
            val context = LocalContext.current
            var searchText by remember { mutableStateOf(TextFieldValue("")) }
            val apps = remember(searchText.text) {
                if (searchText.text.isNotEmpty()) getInstalledApps(context, searchText.text) else emptyList()
            }
            OverlayWithButtons(
                searchText = searchText,
                onOpenURL = { url ->
                    openUrl(url)
                },
                onDismiss = { finish() },
                apps = apps,
                onAppClick = { appInfo ->
                    val launchIntent = context.packageManager.getLaunchIntentForPackage(appInfo.activityInfo.packageName)
                    context.startActivity(launchIntent)
                    finish()
                },
                onSearchTextChange = { searchText = it }
            )
        }
    }

    private fun openUrl(url: String) {
        val pm = packageManager
        val kiwiPackage = "com.kiwibrowser.browser"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val isKiwiInstalled = try {
            pm.getPackageInfo(kiwiPackage, 0)
            true
        } catch (e: Exception) {
            false
        }

        if (isKiwiInstalled) {
            intent.setPackage(kiwiPackage)
        }

        startActivity(intent)
        finish()
    }
}

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
                val context = LocalContext.current
                if (searchText.text.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column {
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
                        apps.forEach { app ->
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