import React from 'react'
import { Linking, StyleSheet, Text, View } from 'react-native'
import { Button, CheckPermissions, Setting } from '~/components'
import { useTranslations } from '~/locale'
import { Launchpad } from '~/modules'
import { colors } from '~/style'
import { APP_SETTINGS_NAMES } from '~/types'

export const Settings = () => {
    const T = useTranslations()

    return (
        <View style={styles.container}>
            <Text style={styles.header}>
                {T.screen.settings.title}
            </Text>
            <View style={styles.settingsContainer}>
                {APP_SETTINGS_NAMES.map(setting => (
                    <Setting
                        key={setting}
                        setting={setting}
                    />
                ))}
            </View>
            <Button onPress={() => Launchpad.open()}>
                {T.screen.settings.openLaunchpad}
            </Button>
            <CheckPermissions />
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.black,
        padding: 16,
        justifyContent: 'center',
        gap: 32,
        position: 'relative'
    },
    header: {
        color: colors.white,
        fontSize: 24,
        textAlign: 'center',
    },
    settingsContainer: {
        gap: 16
    }
})
