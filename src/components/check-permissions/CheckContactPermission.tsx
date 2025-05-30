import React from 'react'
import { Linking } from 'react-native'
import { useTranslations } from '~/locale'
import { Launchpad } from '~/modules'
import { useStore } from '~/store'
import { createStyles } from '~/style'
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
        <Modal
            isVisible
            onClose={() => setIsContactsEnabled(false)}
        >
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

const styles = createStyles(theme => ({
    gapTop: {
        marginTop: theme.gap(2),
    },
    gapBottom: {
        marginBottom: theme.gap(2),
    },
}))
