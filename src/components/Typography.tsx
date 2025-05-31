import React from 'react'
import { StyleProp, TextStyle } from 'react-native'
import Animated from 'react-native-reanimated'
import { colors, createStyles } from '~/style'

type TypographyProps = {
    children: React.ReactNode
    color?: keyof typeof colors
    center?: boolean
    variant?: keyof typeof styles
    style?: StyleProp<TextStyle>
}

export const Typography: React.FunctionComponent<TypographyProps> = ({
    children,
    color = 'white',
    style,
    center,
    variant = 'regular',
}) => {
    return (
        <Animated.Text
            style={[
                styles[variant],
                { color: colors[color], textAlign: center ? 'center' : undefined },
                style,
            ]}
        >
            {children}
        </Animated.Text>
    )
}

const styles = createStyles({
    header: {
        fontSize: 24,
    },
    paragraph: {
        fontSize: 18,
    },
    regular: {
        fontSize: 16,
    },
    tiny: {
        fontSize: 12,
    },
})
