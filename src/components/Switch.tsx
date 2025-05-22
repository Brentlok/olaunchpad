import React, { useEffect } from 'react'
import { Animated, Pressable, StyleSheet, useAnimatedValue } from 'react-native'
import { colors } from '~/style'

type SwitchProps = {
    isEnabled: boolean
    onChange: (isEnabled: boolean) => void
}

export const Switch: React.FC<SwitchProps> = ({
    isEnabled,
    onChange,
}) => {
    const animationProgress = useAnimatedValue(isEnabled ? 1 : 0)
    const backgroundColor = animationProgress.interpolate({
        inputRange: [0, 1],
        outputRange: [colors.gray, colors.primary],
    })
    const translateX = animationProgress.interpolate({
        inputRange: [0, 1],
        outputRange: [-4, 32],
    })

    useEffect(() => {
        const animation = Animated.timing(animationProgress, {
            toValue: isEnabled ? 1 : 0,
            duration: 200,
            useNativeDriver: true,
        })

        animation.start()

        return () => animation.stop()
    }, [isEnabled])

    return (
        <Pressable
            style={styles.container}
            onPress={() => onChange(!isEnabled)}
            hitSlop={8}
        >
            <Animated.View style={[styles.tint, { backgroundColor }]}>
                <Animated.View
                    style={[styles.thumb, { transform: [{ translateX }] }]}
                />
            </Animated.View>
        </Pressable>
    )
}

const styles = StyleSheet.create({
    container: {
        width: 60,
        height: 28,
        maxHeight: 28,
    },
    tint: {
        justifyContent: 'center',
        flex: 1,
        borderRadius: 14,
    },
    thumb: {
        width: 32,
        height: 32,
        borderRadius: 16,
        backgroundColor: colors.white,
    },
})
