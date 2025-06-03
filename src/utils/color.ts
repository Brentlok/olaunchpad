import { colorKit } from 'reanimated-color-picker'
import { colors } from '~/style'

export const getColorsContrast = (colorA: string, colorB: string) => {
    'worklet'

    const compareColor = colorB === colors.black ? { h: 0, s: 0, v: 0 } : { h: 0, s: 0, v: 100 }
    const contrast = colorKit.runOnUI().contrastRatio(colorA, compareColor)
    const reversedColor = colorB === colors.black ? colors.white : colors.black

    return {
        contrast,
        reversedColor,
    }
}
