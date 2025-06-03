package expo.modules.launchpad

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class Settings(
    val isBrowserEnabled: Boolean,
    val isYoutubeEnabled: Boolean,
    val isApplicationsEnabled: Boolean,
    val isContactsEnabled: Boolean,
    val isPlayStoreEnabled: Boolean,
    val isCalculatorEnabled: Boolean,
    val youtubeSearchInBrowser: Boolean,
    val defaultBrowser: String?
)


data class AppColors(
    val accentColor: Color,
    val textColor: Color
)

val LocalAppColors = compositionLocalOf {
    AppColors(
        accentColor = Color.Blue,
        textColor = Color.White
    )
}
