import Animated, { LinearTransition, ReduceMotion } from 'react-native-reanimated'
import { Typography } from '~/components'
import { useTranslations } from '~/locale'
import { createStyles } from '~/style'
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

const styles = createStyles(theme => ({
    container: {
        gap: theme.gap(2),
    },
}))
