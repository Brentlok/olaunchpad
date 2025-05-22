import { StyleSheet } from 'react-native'
import Animated, { LinearTransition, ReduceMotion } from 'react-native-reanimated'
import { Typography } from '~/components'
import { useTranslations } from '~/locale'
import { StyleSetting } from './components'

export const StyleSettings = () => {
    const T = useTranslations()

    return (
        <Animated.View
            layout={LinearTransition.reduceMotion(ReduceMotion.Never)}
            style={styles.container}
        >
            <Typography
                variant="header"
                center
            >
                {T.screen.settings.styleSettings.title}
            </Typography>
            <StyleSetting setting="isBlurEnabled" />
        </Animated.View>
    )
}

const styles = StyleSheet.create({
    container: {
        gap: 16,
    },
})
