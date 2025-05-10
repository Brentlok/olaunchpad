import { Dimensions, StyleSheet, View } from 'react-native'
import { colors } from '~/style'

type ModalProps = {}

export const Modal: React.FC<React.PropsWithChildren<ModalProps>> = ({
    children
}) => {
    return (
        <View style={styles.modal}>
            <View style={styles.contentContainer}>
                {children}
            </View>
        </View>
    )
}

const styles = StyleSheet.create({
    modal: {
        position: 'absolute',
        inset: 0,
        width: Dimensions.get('screen').width,
        height: Dimensions.get('screen').height,
        padding: 16,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)'
    },
    contentContainer: {
        backgroundColor: colors.dark,
        width: '100%',
        justifyContent: 'center',
        alignItems: 'center',
        gap: 16,
        borderRadius: 16,
        padding: 16
    }
})
