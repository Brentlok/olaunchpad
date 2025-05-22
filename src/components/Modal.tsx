import { Portal } from '@gorhom/portal'
import { Dimensions, Pressable, StyleSheet, View } from 'react-native'
import { colors } from '~/style'

type ModalProps = {
    isOpened: boolean
    onClose?: VoidFunction
}

export const Modal: React.FC<React.PropsWithChildren<ModalProps>> = ({
    children,
    isOpened,
    onClose,
}) => {
    if (!isOpened) {
        return null
    }

    return (
        <Portal>
            <View style={styles.modal}>
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
            </View>
        </Portal>
    )
}

const styles = StyleSheet.create({
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
        backgroundColor: colors.dark,
        borderWidth: 1,
        borderColor: colors.primary,
        justifyContent: 'center',
        alignItems: 'center',
        flex: 1,
        gap: 16,
        borderRadius: 16,
        padding: 16,
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
        backgroundColor: colors.white,
        transformOrigin: 'center',
    },
})
