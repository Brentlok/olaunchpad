import { View } from 'react-native'
import { Switch } from '~/components'
import { Typography } from '~/components'
import { useTranslations } from '~/locale'
import { useStore } from '~/store'
import { createStyles } from '~/style'

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

const styles = createStyles(theme => ({
    settingContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: theme.gap(2),
    },
}))
