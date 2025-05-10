import { createStore } from 'stan-js'
import { storage } from 'stan-js/storage'
import { APP_SETTINGS_NAMES, AppSetting, DEFAULT_APP_SETTINGS } from '~/types'

const store = Object.fromEntries(APP_SETTINGS_NAMES.map(name => [name, storage(DEFAULT_APP_SETTINGS[name])])) as {
    [KSetting in AppSetting]: typeof DEFAULT_APP_SETTINGS[KSetting]
}

export const { useStore } = createStore(store)
