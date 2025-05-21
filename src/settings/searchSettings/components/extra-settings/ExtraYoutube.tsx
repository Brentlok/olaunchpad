import { Switch } from "~/components"
import { StyleSheet, View } from "react-native"
import { Typography } from "~/components"
import { useStore } from "~/store"
import { useTranslations } from "~/locale"

export const ExtraYoutube = () => {
    const T = useTranslations()
    const { youtubeSearchInBrowser, setYoutubeSearchInBrowser } = useStore()

    return (
        <View style={styles.settingContainer}>
            <Typography>
                {T.components.extraSettings.youtube.searchInBrowser}
            </Typography>
            <Switch
                isEnabled={youtubeSearchInBrowser}
                onChange={setYoutubeSearchInBrowser}
            />
        </View>
    )
}

const styles = StyleSheet.create({
    settingContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: 16
    }
})
