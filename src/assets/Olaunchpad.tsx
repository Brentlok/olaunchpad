import React, { useState } from 'react'
import { runOnJS, useAnimatedReaction } from 'react-native-reanimated'
import Svg, { Defs, LinearGradient, Path, Stop } from 'react-native-svg'
import { colorKit } from 'reanimated-color-picker'
import { colors } from '~/style'

const mixWithWhite = (rgbaColorInput: string) => {
    'worklet'

    const weight = 0.4
    const rgbaValues = rgbaColorInput.match(/\d+(\.\d+)?/g)?.map(Number)

    if (!rgbaValues || rgbaValues.length !== 4) {
        return ''
    }

    const [r1, g1, b1, a1] = rgbaValues

    const r = Math.round(r1 * weight + 255 * (1 - weight))
    const g = Math.round(g1 * weight + 255 * (1 - weight))
    const b = Math.round(b1 * weight + 255 * (1 - weight))

    return `rgba(${r}, ${g}, ${b}, ${a1})`
}

export const Olaunchpad = () => {
    const [darkColor, setDarkColor] = useState(colors.accent.get())
    const [lightColor, setLightColor] = useState(mixWithWhite(colorKit.runOnUI().RGB(colors.accent.get()).string(true)))

    useAnimatedReaction(
        () => colors.accent.value,
        accentColor => {
            const accentColorRgba = colorKit.runOnUI().RGB(accentColor).string(true)

            runOnJS(setDarkColor)(accentColorRgba)
            runOnJS(setLightColor)(mixWithWhite(accentColorRgba))
        },
    )

    return (
        <Svg
            width={80}
            height={80}
            fill="none"
            viewBox="0 0 1024 1024"
            style={{ marginHorizontal: 'auto' }}
        >
            <Path
                fill="url(#a)"
                d="M512 62c2.597 0 5.188.022 7.774.066 27.77.47 48.476 24.263 48.476 52.036v8.558c0 29.569-26.681 51.84-56.25 51.84-186.396 0-337.5 151.104-337.5 337.5S325.604 849.5 512 849.5 849.5 698.396 849.5 512c0-29.569 22.271-56.25 51.84-56.25h8.557c27.774 0 51.566 20.706 52.037 48.475.044 2.587.066 5.178.066 7.775 0 248.528-201.472 450-450 450S62 760.528 62 512 263.472 62 512 62Z"
            />
            <Path
                fill="url(#b)"
                d="M514.808 343.491C718.306 93.434 875.393 133.916 882.537 135.108c8.331 2.382 15.472 9.526 17.852 17.861 2.381 7.15 41.648 164.329-208.262 367.946l8.33 58.346v3.573c0 30.959-14.28 59.538-40.462 77.399l-91.635 63.11c-8.33 4.763-16.661 8.336-26.181 8.336-11.901 0-23.802-4.763-32.132-13.098-11.901-11.908-16.661-28.579-11.9-45.249l16.661-57.157c3.57-13.098 0-26.196-9.521-34.532l-49.983-50.012c-9.521-9.526-22.611-13.098-35.701-9.526l-57.124 16.671c-15.471 4.763-33.322-.001-45.222-11.908-15.471-15.48-17.851-40.486-4.761-58.347l63.073-92.88c17.851-25.006 46.413-40.485 77.355-40.485h3.57l58.314 8.335Zm233.195-97.493c-22.091 0-40 17.908-40 40 0 22.091 17.909 40 40 40s40-17.909 40-40c0-22.092-17.909-40-40-40Z"
            />
            <Defs>
                <LinearGradient
                    id="a"
                    x1={959.894}
                    x2={64.106}
                    y1={62}
                    y2={962}
                    gradientUnits="userSpaceOnUse"
                >
                    <Stop stopColor={lightColor} />
                    <Stop offset={1} stopColor={darkColor} />
                </LinearGradient>
                <LinearGradient
                    id="b"
                    x1={902.595}
                    x2={305.724}
                    y1={132}
                    y2={731.998}
                    gradientUnits="userSpaceOnUse"
                >
                    <Stop stopColor={lightColor} />
                    <Stop offset={1} stopColor={darkColor} />
                </LinearGradient>
            </Defs>
        </Svg>
    )
}
