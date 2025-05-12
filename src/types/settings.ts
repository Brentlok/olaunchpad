export type AppSetting = keyof typeof DEFAULT_APP_SETTINGS

export const DEFAULT_APP_SETTINGS = {
    isBrowserEnabled: true,
    isYoutubeEnabled: true,
    isApplicationsEnabled: true,
    isContactsEnabled: false,
    isPlayStoreEnabled: true,
    isCalculatorEnabled: true,
}

export const APP_SETTINGS_NAMES = Object.keys(DEFAULT_APP_SETTINGS) as Array<AppSetting>
