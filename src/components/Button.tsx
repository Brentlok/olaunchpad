import React from 'react'
import { Pressable, PressableStateCallbackType, StyleProp, Text, View, ViewStyle } from 'react-native'
import Animated from 'react-native-reanimated'
import { createStyles } from '~/style'

type ButtonProps = {
    style?: StyleProp<ViewStyle>
    children: string
    variant?: Variant
    isTiny?: boolean
    onPress?: VoidFunction
}

type Variant = 'primary' | 'outline'

export const Button: React.FC<ButtonProps> = ({
    variant = 'primary',
    isTiny = false,
    style,
    children,
    onPress,
}) => {
    return (
        <Pressable
            style={state => [styles.buttonContainer(state, isTiny), style]}
            onPress={onPress}
        >
            <Animated.View
                style={[
                    styles.button,
                    styles[variant],
                ]}
            >
                <Animated.Text style={styles.buttonText(isTiny, variant)}>
                    {children}
                </Animated.Text>
            </Animated.View>
        </Pressable>
    )
}

const styles = createStyles(theme => ({
    button: {
        flexGrow: 1,
        width: '100%',
        height: '100%',
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: theme.gap(1),
        paddingHorizontal: theme.gap(1),
    },
    buttonText: (isTiny: boolean, variant: Variant) => ({
        textAlign: 'center',
        fontSize: isTiny ? 12 : 18,
        color: variant === 'primary' ? theme.colors.textColor : theme.colors.white,
    }),
    primary: {
        backgroundColor: theme.colors.accent,
    },
    outline: {
        borderWidth: 1,
        borderColor: theme.colors.white,
    },
    buttonContainer: (state: PressableStateCallbackType, isTiny: boolean) => ({
        opacity: state.pressed ? 0.8 : 1,
        flex: isTiny ? undefined : 1,
        width: isTiny ? 'auto' : '100%',
        height: isTiny ? 32 : 56,
        minHeight: isTiny ? 32 : 56,
        maxHeight: isTiny ? 32 : 56,
    }),
}))
