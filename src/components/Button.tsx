import React from 'react'
import { Pressable, PressableStateCallbackType } from 'react-native'
import { createStyles } from '~/style'
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
            style={styles.button}
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

const styles = createStyles(theme => ({
    button: (state: PressableStateCallbackType) => ({
        flexGrow: 1,
        height: 56,
        minHeight: 56,
        maxHeight: 56,
        width: '100%',
        backgroundColor: theme.colors.primary,
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: theme.gap(1),
        opacity: state.pressed ? 0.8 : 1,
    }),
}))
