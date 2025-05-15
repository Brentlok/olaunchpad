import React from 'react'
import { Dimensions, StatusBar, StyleSheet, View } from 'react-native'
import { Button, CheckPermissions } from '~/components'
import { useTranslations } from '~/locale'
import { Launchpad } from '~/modules'
import { SearchSettings, StyleSettings } from '~/settings'
import { colors } from '~/style'

export const Settings = () => {
    const T = useTranslations()

    return (
        <View style={styles.container}>
            <View style={styles.settingsContainer}>
                <SearchSettings />
                <StyleSettings />
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
        position: 'relative',
        paddingTop: StatusBar.currentHeight,
        paddingBottom: Dimensions.get('screen').height - Dimensions.get('window').height - (StatusBar.currentHeight ?? 0)
    },
    settingsContainer: {
        flex: 1,
        gap: 16
    }
})
