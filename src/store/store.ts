import { createStore } from 'stan-js'
import { storage } from 'stan-js/storage'
import { Launchpad } from '~/modules'
import { SEARCH_SETTINGS_NAMES, SearchSetting, DEFAULT_SEARCH_SETTINGS, StyleSetting, DEFAULT_STYLE_SETTINGS, STYLE_SETTINGS_NAMES, HistoryItem } from '~/types'

const searchSettings = Object.fromEntries(SEARCH_SETTINGS_NAMES.map(name => [name, storage(DEFAULT_SEARCH_SETTINGS[name])])) as {
    [KSetting in SearchSetting]: typeof DEFAULT_SEARCH_SETTINGS[KSetting]
}

const styleSettings = Object.fromEntries(STYLE_SETTINGS_NAMES.map(name => [name, storage(DEFAULT_STYLE_SETTINGS[name])])) as {
    [KSetting in StyleSetting]: typeof DEFAULT_STYLE_SETTINGS[KSetting]
}

export const { useStore } = createStore({
    ...searchSettings,
    ...styleSettings,
    history: storage<Array<HistoryItem>>([]),
    youtubeSearchInBrowser: storage(false),
    defaultBrowser: storage<string>(Launchpad.getDefaultBrowser())
})
