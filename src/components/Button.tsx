import React from 'react'
import { Pressable, StyleSheet } from 'react-native'
import { colors } from '~/style'
import { Typography } from './Typography'

type ButtonProps = {
    children: string
    onPress: VoidFunction
}

export const Button: React.FC<ButtonProps> = ({
    children,
    onPress,
}) => {
    return (
        <Pressable
            style={state => [styles.button, state.pressed && { opacity: 0.8 }]}
            onPress={onPress}
        >
            <Typography
                variant="button"
                center
            >
                {children}
            </Typography>
        </Pressable>
    )
}

const styles = StyleSheet.create({
    button: {
        flexGrow: 1,
        height: 56,
        minHeight: 56,
        maxHeight: 56,
        width: '100%',
        backgroundColor: colors.primary,
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: 8,
    },
})
