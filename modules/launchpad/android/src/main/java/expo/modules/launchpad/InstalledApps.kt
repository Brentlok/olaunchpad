package expo.modules.launchpad
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

data class InstalledApp(
    val label: String,
    val icon: ImageBitmap,
    val packageName: String,
)

data class InstalledAppsState(
    val installedApps: List<InstalledApp>,
    val onAppPress: (app: InstalledApp) -> Unit,
    val openApp: (packageName: String) -> Unit
)

@Composable
fun getInstalledAppsState(launchpad: LaunchpadState): InstalledAppsState {
    var allApps by remember { mutableStateOf<List<InstalledApp>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

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
        openApp = ::openApp
    )
}

@Composable
fun InstalledAppView(app: InstalledApp, installedAppsState: InstalledAppsState) {
    LaunchpadRowItem(
        icon = app.icon,
        label = app.label,
        onClick = { installedAppsState.onAppPress(app) },
        subLabel = null
    )
}