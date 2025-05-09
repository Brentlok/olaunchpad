import React from 'react'
import { Pressable, StyleSheet, Text } from 'react-native'
import { colors } from '../colors'

type ButtonProps = {
    children: string,
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
            <Text style={styles.buttonText}>
                {children}
            </Text>
        </Pressable>
    )
}

const styles = StyleSheet.create({
    button: {
        flex: 1,
        height: 56,
        maxHeight: 56,
        backgroundColor: colors.primary,
        justifyContent: 'center',
        alignItems: 'center',
        borderRadius: 8,
    },
    buttonText: {
        color: colors.white,
        fontSize: 18,
        fontWeight: 'bold',
    }
})
