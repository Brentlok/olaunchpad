import React from 'react'
import { Pressable, StyleSheet, Text, View } from 'react-native'
import { colors } from '~/style'

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
    buttonText: {
        color: colors.white,
        fontSize: 18,
        fontWeight: 'bold',
    }
})
