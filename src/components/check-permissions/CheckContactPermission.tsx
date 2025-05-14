import React from 'react'
import { Launchpad } from '~/modules'
import { StyleSheet, Text, Linking } from 'react-native'
import { useStore } from '~/store'
import { useTranslations } from '~/locale'
import { Modal } from '../Modal'
import { Button } from '../Button'
import { Typography } from '../Typography'

type CheckContactPermissionProps = {
    setHasContactsPermission: (hasContactsPermission: boolean) => void
}

export const CheckContactPermission: React.FC<CheckContactPermissionProps> = ({ setHasContactsPermission }) => {
    const T = useTranslations()
    const { setIsContactsEnabled } = useStore()
    
    const handlePermissionRequest = () => {
        const start = Date.now()

        Launchpad.requestReadContactsPermission()
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
            <Typography
                variant='paragraph'
                style={styles.gapBottom}
            >
                {T.components.checkPermissions.contacts.description}
            </Typography>
            <Typography variant='paragraph'>
                {T.components.checkPermissions.contacts.grant}
            </Typography>
            <Button onPress={handlePermissionRequest}>
                {T.components.checkPermissions.contacts.grantButton}
            </Button>
            <Typography
                variant='paragraph'
                style={styles.gapTop}
            >
                {T.components.checkPermissions.contacts.deny}
            </Typography>
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
    }
})
