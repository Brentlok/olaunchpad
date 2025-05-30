package expo.modules.launchpad

import android.content.pm.ResolveInfo
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class InstalledApp(
    val label: String,
    val resolveInfo: ResolveInfo,
    val packageName: String,
)

data class InstalledAppsState(
    val installedApps: List<InstalledApp>,
    val onAppPress: (app: InstalledApp) -> Unit,
    val openApp: (packageName: String) -> Unit,
    val launchpad: LaunchpadState,
    val iconCache: SnapshotStateMap<String, ImageBitmap>
)

@Composable
fun getInstalledAppsState(launchpad: LaunchpadState): InstalledAppsState {
    var allApps by remember { mutableStateOf<List<InstalledApp>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()
    val iconCache = remember { mutableStateMapOf<String, ImageBitmap>() }

    val apps = remember(launchpad.searchText.text, allApps) {
        filterAndTake(
            list = allApps,
            callback =  {
                it.label.contains(launchpad.searchText.text, ignoreCase = true) ||
                it.packageName.contains(launchpad.searchText.text, ignoreCase = true)
            },
            count = 3
        )
    }

    fun openApp(packageName: String) {
        val launchIntent = launchpad.context.packageManager.getLaunchIntentForPackage(packageName)
        launchpad.context.startActivity(launchIntent)
        launchpad.closeLaunchpad()
    }

    fun onAppPress(app: InstalledApp) {
        launchpad.saveLastAction(
            HistoryItem(
            type = "app",
            label = "Open ${app.label}",
            actionValue = app.packageName
        )
        )
        openApp(app.packageName)
    }

    LaunchedEffect(key1 = true) {
        if (launchpad.settings.isApplicationsEnabled) {
            coroutineScope.launch {
                try {
                    val fetchedAppsList = getInstalledApps(launchpad.context)
                    allApps = fetchedAppsList
                } catch (e: Exception) {
                    allApps = emptyList()
                }
            }
        }
    }

    return InstalledAppsState(
        apps,
        onAppPress = ::onAppPress,
        openApp = ::openApp,
        launchpad = launchpad,
        iconCache = iconCache
    )
}

@Composable
fun InstalledAppView(app: InstalledApp, installedAppsState: InstalledAppsState) {
    val context = installedAppsState.launchpad.context
    val packageManager = context.packageManager

    val iconBitmap by produceState(
        initialValue = installedAppsState.iconCache[app.packageName],
        key1 = app.packageName,
        key2 = packageManager,
    ) {
        if (value != null) {
            return@produceState
        }

        try {
            val loadedBitmap = withContext(Dispatchers.IO) {
                val iconDrawable = app.resolveInfo.loadIcon(packageManager)
                drawableToImageBitmap(iconDrawable)
            }
            installedAppsState.iconCache[app.packageName] = loadedBitmap
            value = loadedBitmap
        } catch (e: Exception) {
            value = null
        }
    }

    LaunchpadRowItem(
        icon = iconBitmap,
        label = app.label,
        onClick = { installedAppsState.onAppPress(app) },
        subLabel = null,
    )
}