import React from 'react'
import { StyleProp, Text, TextStyle } from 'react-native'
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
        <Text
            style={[
                styles[variant],
                { color: colors[color], textAlign: center ? 'center' : undefined },
                style,
            ]}
        >
            {children}
        </Text>
    )
}

const styles = createStyles({
    header: {
        fontSize: 24,
    },
    button: {
        fontSize: 18,
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
