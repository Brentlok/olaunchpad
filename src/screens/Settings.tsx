import React from 'react'
import { StyleSheet, Text, View } from 'react-native'
import { CheckPermissions, Setting } from '~/components'
import { useTranslations } from '~/locale'
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
