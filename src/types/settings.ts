export type SearchSetting = keyof typeof DEFAULT_SEARCH_SETTINGS
export type StyleSetting = keyof typeof DEFAULT_STYLE_SETTINGS

export const DEFAULT_SEARCH_SETTINGS = {
    isBrowserEnabled: true,
    isYoutubeEnabled: true,
    isApplicationsEnabled: true,
    isContactsEnabled: false,
    isPlayStoreEnabled: true,
    isCalculatorEnabled: true,
}

export const DEFAULT_STYLE_SETTINGS = {
    isBlurEnabled: false,
}

export const SEARCH_SETTINGS_NAMES = Object.keys(DEFAULT_SEARCH_SETTINGS) as Array<SearchSetting>
export const STYLE_SETTINGS_NAMES = Object.keys(DEFAULT_STYLE_SETTINGS) as Array<StyleSetting>
