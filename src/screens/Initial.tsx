import { Linking, StyleSheet, View } from 'react-native'
import { Button, Typography } from '~/components'
import { useTranslations } from '~/locale'
import { colors } from '~/style'

export const Initial = () => {
    const T = useTranslations()

    return (
        <View style={styles.container}>
            <Typography
                variant="paragraph"
                center
            >
                {T.screen.initial.title}
            </Typography>
            <Button onPress={() => Linking.sendIntent('android.settings.VOICE_INPUT_SETTINGS')}>
                {T.screen.initial.openSettings}
            </Button>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.black,
        padding: 16,
        justifyContent: 'center',
        gap: 32,
    },
})
