import React from 'react'
import { Linking, StyleSheet, Text } from 'react-native'
import { useTranslations } from '~/locale'
import { Launchpad } from '~/modules'
import { useStore } from '~/store'
import { Button } from '../Button'
import { Modal } from '../Modal'
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
        <Modal isOpened>
            <Typography
                variant="paragraph"
                style={styles.gapBottom}
            >
                {T.components.checkPermissions.contacts.description}
            </Typography>
            <Typography variant="paragraph">
                {T.components.checkPermissions.contacts.grant}
            </Typography>
            <Button onPress={handlePermissionRequest}>
                {T.components.checkPermissions.contacts.grantButton}
            </Button>
            <Typography
                variant="paragraph"
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
        marginTop: 16,
    },
    gapBottom: {
        marginBottom: 16,
    },
})
