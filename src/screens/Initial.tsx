import { Image, Linking, StyleSheet } from 'react-native'
import Animated, { SlideInLeft, SlideOutLeft } from 'react-native-reanimated'
import { Images } from '~/assets'
import { Button, Typography } from '~/components'
import { useTranslations } from '~/locale'
import { colors } from '~/style'

export const Initial = () => {
    const T = useTranslations()

    return (
        <Animated.View
            entering={SlideInLeft}
            exiting={SlideOutLeft}
            style={styles.container}
        >
            <Image
                source={Images.logo}
                style={styles.logo}
            />
            <Typography
                variant="paragraph"
                center
            >
                {T.screen.initial.title}
            </Typography>
            <Button onPress={() => Linking.sendIntent('android.settings.VOICE_INPUT_SETTINGS')}>
                {T.screen.initial.openSettings}
            </Button>
        </Animated.View>
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
    logo: {
        width: 80,
        height: 80,
        marginHorizontal: 'auto',
    },
})
