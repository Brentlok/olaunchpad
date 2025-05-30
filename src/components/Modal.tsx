import { Portal } from '@gorhom/portal'
import { Dimensions, Pressable, View } from 'react-native'
import Animated, { FadeIn, FadeOut, ReduceMotion } from 'react-native-reanimated'
import { useBackHandler } from '~/hooks'
import { createStyles } from '~/style'

type ModalProps = {
    isVisible: boolean
    onClose?: VoidFunction
}

export const Modal: React.FC<React.PropsWithChildren<ModalProps>> = ({
    children,
    isVisible,
    onClose,
}) => {
    useBackHandler(() => {
        if (!isVisible) {
            return false
        }

        onClose?.()

        return true
    })

    if (!isVisible) {
        return null
    }

    return (
        <Portal>
            <Animated.View
                entering={FadeIn.reduceMotion(ReduceMotion.Never).duration(300)}
                exiting={FadeOut.reduceMotion(ReduceMotion.Never).duration(300)}
                style={styles.modal}
            >
                <Pressable
                    style={styles.backgroundPress}
                    onPress={onClose}
                />
                <View style={styles.contentContainer}>
                    <Pressable
                        style={styles.backgroundSidePress}
                        onPress={onClose}
                    />
                    <View style={styles.content}>
                        {onClose && (
                            <Pressable
                                style={styles.close}
                                onPress={onClose}
                                hitSlop={8}
                            >
                                <View style={[styles.closeIcon, { marginTop: 8, transform: [{ rotate: '45deg' }] }]} />
                                <View style={[styles.closeIcon, { marginTop: -2, transform: [{ rotate: '-45deg' }] }]} />
                            </Pressable>
                        )}
                        {children}
                    </View>
                    <Pressable
                        style={styles.backgroundSidePress}
                        onPress={onClose}
                    />
                </View>
                <Pressable
                    style={styles.backgroundPress}
                    onPress={onClose}
                />
            </Animated.View>
        </Portal>
    )
}

const styles = createStyles(theme => ({
    modal: {
        position: 'absolute',
        inset: 0,
        width: Dimensions.get('screen').width,
        height: Dimensions.get('screen').height,
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    contentContainer: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    content: {
        backgroundColor: theme.colors.dark,
        borderWidth: 1,
        borderColor: theme.colors.primary,
        justifyContent: 'center',
        alignItems: 'center',
        flex: 1,
        gap: theme.gap(2),
        borderRadius: theme.gap(2),
        paddingVertical: theme.gap(4),
        paddingHorizontal: theme.gap(2),
    },
    backgroundPress: {
        flex: 1,
    },
    backgroundSidePress: {
        width: 16,
        height: '100%',
    },
    close: {
        position: 'absolute',
        top: 8,
        right: 8,
    },
    closeIcon: {
        width: 20,
        height: 2,
        backgroundColor: theme.colors.white,
        transformOrigin: 'center',
    },
}))
