import { StyleSheet, View } from "react-native"
import { Typography } from "~/components"
import { useTranslations } from "~/locale"
import { SEARCH_SETTINGS_NAMES } from "~/types"
import { SearchSetting } from "./components"

export const SearchSettings = () => {
    const T = useTranslations()
    
    return (
        <>
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
        </>
    )
}

const styles = StyleSheet.create({
    settingsContainer: {
        gap: 16,
    },
})
