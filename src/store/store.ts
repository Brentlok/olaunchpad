import { ColorValue } from 'react-native'
import { createStore } from 'stan-js'
import { storage } from 'stan-js/storage'
import { Launchpad } from '~/modules'
import {
    DEFAULT_SEARCH_SETTINGS,
    DEFAULT_STYLE_SETTINGS,
    HistoryItem,
    SEARCH_SETTINGS_NAMES,
    SearchSetting,
    STYLE_SETTINGS_NAMES,
    StyleSetting,
} from '~/types'

const searchSettings = Object.fromEntries(SEARCH_SETTINGS_NAMES.map(name => [name, storage(DEFAULT_SEARCH_SETTINGS[name])])) as {
    [KSetting in SearchSetting]: typeof DEFAULT_SEARCH_SETTINGS[KSetting]
}

const styleSettings = Object.fromEntries(STYLE_SETTINGS_NAMES.map(name => [name, storage(DEFAULT_STYLE_SETTINGS[name])])) as {
    [KSetting in StyleSetting]: typeof DEFAULT_STYLE_SETTINGS[KSetting]
}

export const { useStore, getState } = createStore({
    ...searchSettings,
    ...styleSettings,
    accentColor: storage('#9B7EBD'),
    textColor: storage('#EEEEEE'),
    history: storage<Array<HistoryItem>>([]),
    youtubeSearchInBrowser: storage(false),
    defaultBrowser: storage<string>(Launchpad.getDefaultBrowser()),
})
