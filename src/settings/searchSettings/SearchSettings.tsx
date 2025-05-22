import { StyleSheet, View } from 'react-native'
import { Typography } from '~/components'
import { useTranslations } from '~/locale'
import { SEARCH_SETTINGS_NAMES } from '~/types'
import { SearchSetting } from './components'

export const SearchSettings = () => {
    const T = useTranslations()

    return (
        <View style={styles.container}>
            <Typography
                variant="header"
                center
            >
                {T.screen.settings.searchSettings.title}
            </Typography>
            <View style={styles.settingsContainer}>
                {SEARCH_SETTINGS_NAMES.map(setting => (
                    <SearchSetting
                        key={setting}
                        setting={setting}
                    />
                ))}
            </View>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        gap: 16,
    },
    settingsContainer: {
        gap: 16,
    },
})
