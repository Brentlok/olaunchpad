import type { AnimatedStyle } from 'react-native-reanimated'
import { colors } from './colors'

const theme = {
    colors,
    gap: (v: number) => v * 8,
}

type Theme = typeof theme

export const createStyles = <T extends Record<string, AnimatedStyle | ((...params: Array<any>) => AnimatedStyle)>>(
    stylesheet: ((theme: Theme) => T) | T,
) => {
    const computedStylesheet = typeof stylesheet === 'function'
        ? stylesheet(theme)
        : stylesheet

    return computedStylesheet
}
