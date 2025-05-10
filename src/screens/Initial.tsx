import { Linking, StyleSheet, Text, View } from 'react-native'
import { Button } from '~/components'
import { useTranslations } from '~/locale'
import { colors } from '~/style'

export const Initial = () => {
    const T = useTranslations()
    
    return (
        <View style={styles.container}>
            <Text style={styles.header}>
                {T.screen.initial.title}
            </Text>
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
        gap: 32
    },
    header: {
        color: colors.white,
        fontSize: 18,
        textAlign: 'center',
    },
})
