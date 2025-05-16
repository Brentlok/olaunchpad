package expo.modules.launchpad

data class Settings(
    val isBrowserEnabled: Boolean,
    val isYoutubeEnabled: Boolean,
    val isApplicationsEnabled: Boolean,
    val isContactsEnabled: Boolean,
    val isPlayStoreEnabled: Boolean,
    val isCalculatorEnabled: Boolean,
)

data class HistoryItem(
    val type: String,
    val label: String,
    val actionValue: String
)
