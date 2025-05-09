import { StyleSheet, Text, View } from 'react-native'
import { OverlayModule } from 'overlay-module'
import { Button } from './components'
import { colors } from './colors'
import React, { useEffect, useState } from 'react'
import { useWillBeActive } from './useWillBeActive'

export const App = () => {
    const [hasContactsPermission, setHasContactsPermission] = useState(() => OverlayModule.getHasReadContactsPermission())
    const [isDefaultAssistant, setIsDefaultAssistant] = useState(() => OverlayModule.getIsDefaultAssistant())
    const { willBeActive } = useWillBeActive()

    const handlePermissionRequest = () => {
        OverlayModule.requestReadContactsPermission()
            .then(() => setHasContactsPermission(true))
            .catch(() => setHasContactsPermission(false))
    }

    useEffect(() => {
        if (!willBeActive) {
            return
        }

        setIsDefaultAssistant(OverlayModule.getIsDefaultAssistant())
    }, [willBeActive])

    const everythingSetup = [
        isDefaultAssistant,
        hasContactsPermission
    ].every(Boolean)

    if (everythingSetup) {
        return (
            <View style={styles.container}>
                <Text style={styles.header}>
                    {'Olaunchpad is ready to use.\nLong press the home button to start.'}
                </Text>
            </View>
        )
    }

    return (
        <View style={styles.container}>
            {!isDefaultAssistant && (
                <React.Fragment>
                    <Text style={styles.header}>
                        You must set Olaunchpad as the default assistant in the system settings.
                    </Text>
                    <Button onPress={() => OverlayModule.openAssistantSettings()}>
                        Open Default Assistant Settings
                    </Button>
                </React.Fragment>
            )}
            {!hasContactsPermission && (
                <React.Fragment>
                    <Text style={styles.header}>
                        You must allow Olaunchpad to read your contacts in the system settings to search for them.
                    </Text>
                    <Button onPress={handlePermissionRequest}>
                        Grant Read Contacts Permission
                    </Button>
                </React.Fragment>
            )}
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.black,
        padding: 16,
        justifyContent: 'center',
        gap: 32
    },
    header: {
        color: colors.white,
        fontSize: 18,
        textAlign: 'center',
    }
})
