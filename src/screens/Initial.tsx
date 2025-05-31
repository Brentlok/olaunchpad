import { Image, Linking } from 'react-native'
import Animated, { SlideInLeft, SlideOutLeft } from 'react-native-reanimated'
import { Images } from '~/assets'
import { Button, Typography } from '~/components'
import { useTranslations } from '~/locale'
import { createStyles } from '~/style'

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

const styles = createStyles(theme => ({
    container: {
        flex: 1,
        backgroundColor: theme.colors.black,
        padding: theme.gap(2),
        justifyContent: 'center',
        gap: theme.gap(4),
    },
    logo: {
        width: 80,
        height: 80,
        marginHorizontal: 'auto',
    },
}))
