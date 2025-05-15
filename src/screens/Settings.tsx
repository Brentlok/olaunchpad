import React from 'react'
import { Dimensions, ScrollView, StatusBar, StyleSheet, View } from 'react-native'
import { Button, CheckPermissions } from '~/components'
import { useTranslations } from '~/locale'
import { Launchpad } from '~/modules'
import { SearchSettings, StyleSettings } from '~/settings'
import { colors } from '~/style'

export const Settings = () => {
    const T = useTranslations()

    return (
        <View style={styles.container}>
            <ScrollView
                style={styles.scrollView}
                contentContainerStyle={styles.settingsContainer}
            >
                <SearchSettings />
                <StyleSettings />
                <View style={styles.spacer} />
            </ScrollView>
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
    scrollView: {
        marginHorizontal: -16
    },
    settingsContainer: {
        gap: 16,
        paddingHorizontal: 16
    },
    spacer: {
        height: 32
    }
})
