import React from 'react'
import { OverlayModule } from 'overlay-module'
import { StyleSheet, Text, Linking } from 'react-native'
import { useStore } from '~/store'
import { useTranslations } from '~/locale'
import { Modal } from '../Modal'
import { Button } from '../Button'

type CheckContactPermissionProps = {
    setHasContactsPermission: (hasContactsPermission: boolean) => void
}

export const CheckContactPermission: React.FC<CheckContactPermissionProps> = ({ setHasContactsPermission }) => {
    const T = useTranslations()
    const { setIsContactsEnabled } = useStore()
    
    const handlePermissionRequest = () => {
        const start = Date.now()

        OverlayModule.requestReadContactsPermission()
            .then(() => setHasContactsPermission(true))
            .catch(() => {
                if (Date.now() - start < 500) {
                    Linking.openSettings()
                }

                setHasContactsPermission(false)
            })
    }

    return (
        <Modal>
            <Text style={[styles.text, styles.gapBottom]}>
                {T.components.checkPermissions.contacts.description}
            </Text>
            <Text style={styles.text}>
                {T.components.checkPermissions.contacts.grant}
            </Text>
            <Button onPress={handlePermissionRequest}>
                {T.components.checkPermissions.contacts.grantButton}
            </Button>
            <Text style={[styles.text, styles.gapTop]}>
                {T.components.checkPermissions.contacts.deny}
            </Text>
            <Button onPress={() => setIsContactsEnabled(false)}>
                {T.components.checkPermissions.contacts.denyButton}
            </Button>
        </Modal>
    )
}

const styles = StyleSheet.create({
    gapTop: {
        marginTop: 16
    },
    gapBottom: {
        marginBottom: 16
    },
    text: {
        fontSize: 18,
        color: 'white',
        textAlign: 'center'
    }
})
