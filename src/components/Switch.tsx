import React, { useEffect } from 'react'
import { Pressable } from 'react-native'
import Animated, { interpolate, interpolateColor, ReduceMotion, useAnimatedStyle, useSharedValue, withTiming } from 'react-native-reanimated'
import { colors, createStyles } from '~/style'

type SwitchProps = {
    isEnabled: boolean
    onChange?: (isEnabled: boolean) => void
}

export const Switch: React.FC<SwitchProps> = ({
    isEnabled,
    onChange,
}) => {
    const animationProgress = useSharedValue(isEnabled ? 1 : 0)
    const animatedBackground = useAnimatedStyle(() => ({
        backgroundColor: interpolateColor(
            animationProgress.value,
            [0, 1],
            [colors.gray, colors.accent.get()],
        ),
    }))
    const animatedThumb = useAnimatedStyle(() => ({
        transform: [
            {
                translateX: interpolate(
                    animationProgress.value,
                    [0, 1],
                    [-4, 32],
                ),
            },
        ],
    }))

    useEffect(() => {
        animationProgress.set(withTiming(
            isEnabled ? 1 : 0,
            {
                duration: 200,
                reduceMotion: ReduceMotion.Never,
            },
        ))
    }, [isEnabled])

    return (
        <Pressable
            style={styles.container}
            onPress={() => onChange?.(!isEnabled)}
            hitSlop={8}
        >
            <Animated.View style={[styles.tint, animatedBackground]}>
                <Animated.View
                    style={[styles.thumb, animatedThumb]}
                />
            </Animated.View>
        </Pressable>
    )
}

const styles = createStyles(theme => ({
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
        backgroundColor: theme.colors.white,
    },
}))
