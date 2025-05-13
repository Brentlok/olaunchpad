package expo.modules.launchpad
import androidx.compose.ui.graphics.ImageBitmap

data class Contact(
    val label: String,
    val phoneNumber: String?,
    val icon: ImageBitmap?
)

data class Settings(
    val isBrowserEnabled: Boolean,
    val isYoutubeEnabled: Boolean,
    val isApplicationsEnabled: Boolean,
    val isContactsEnabled: Boolean,
    val isPlayStoreEnabled: Boolean,
    val isCalculatorEnabled: Boolean,
)

data class InstalledApp(
    val label: String,
    val icon: ImageBitmap,
    val packageName: String,
)