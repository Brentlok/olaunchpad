import React from 'react'
import { Dimensions, Linking, View } from 'react-native'
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
            <View style={styles.container}>
                <Typography
                    variant="paragraph"
                    style={styles.gapBottom}
                >
                    {T.components.checkPermissions.contacts.description}
                </Typography>
                <Typography variant="paragraph">
                    {T.components.checkPermissions.contacts.grant}
                </Typography>
                <View style={styles.buttonContainer}>
                    <Button onPress={handlePermissionRequest}>
                        {T.components.checkPermissions.contacts.grantButton}
                    </Button>
                </View>
                <Typography
                    variant="paragraph"
                    style={styles.gapTop}
                >
                    {T.components.checkPermissions.contacts.deny}
                </Typography>
                <View style={styles.buttonContainer}>
                    <Button onPress={() => setIsContactsEnabled(false)}>
                        {T.components.checkPermissions.contacts.denyButton}
                    </Button>
                </View>
            </View>
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
    container: {
        gap: theme.gap(2),
    },
    buttonContainer: {
        width: Dimensions.get('window').width - theme.gap(6),
        height: 56,
    },
}))
