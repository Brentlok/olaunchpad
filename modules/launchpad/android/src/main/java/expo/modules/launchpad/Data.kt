package expo.modules.launchpad
import android.net.Uri

data class Contact(
    val label: String,
    val phoneNumber: String?,
    val photoUri: Uri?
)

data class Settings(
    val isBrowserEnabled: Boolean,
    val isYoutubeEnabled: Boolean,
    val isApplicationsEnabled: Boolean,
    val isContactsEnabled: Boolean,
    val isPlayStoreEnabled: Boolean,
)
