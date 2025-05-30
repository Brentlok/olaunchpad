import { ImageStyle, TextStyle, ViewStyle } from 'react-native'
import { colors } from './colors'

type AnyStyle = ViewStyle | TextStyle | ImageStyle

const theme = {
    colors,
    gap: (v: number) => v * 8,
}

type Theme = typeof theme

export const createStyles = <T extends Record<string, AnyStyle | ((...params: Array<any>) => AnyStyle)>>(stylesheet: ((theme: Theme) => T) | T) => {
    const computedStylesheet = typeof stylesheet === 'function'
        ? stylesheet(theme)
        : stylesheet

    return computedStylesheet
}
