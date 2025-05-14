import React from 'react'
import { StyleSheet, View } from 'react-native'
import { Button, CheckPermissions } from '~/components'
import { useTranslations } from '~/locale'
import { Launchpad } from '~/modules'
import { SearchSettings, StyleSettings } from '~/settings'
import { colors } from '~/style'

export const Settings = () => {
    const T = useTranslations()

    return (
        <View style={styles.container}>
            <SearchSettings />
            <Button onPress={() => Launchpad.open()}>
                {T.screen.settings.openLaunchpad}
            </Button>
            <StyleSettings />
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
    }
})
