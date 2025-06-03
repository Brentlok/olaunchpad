import React from 'react'
import { ScrollView, StatusBar, View } from 'react-native'
import Animated, { SlideInRight, SlideOutRight } from 'react-native-reanimated'
import { useSafeAreaInsets } from 'react-native-safe-area-context'
import { Olaunchpad } from '~/assets'
import { Button, CheckPermissions } from '~/components'
import { useTranslations } from '~/locale'
import { Launchpad } from '~/modules'
import { SearchSettings, StyleSettings } from '~/settings'
import { createStyles } from '~/style'

export const Settings = () => {
    const T = useTranslations()
    const insets = useSafeAreaInsets()

    return (
        <Animated.View
            entering={SlideInRight}
            exiting={SlideOutRight}
            style={[
                styles.container,
                {
                    paddingTop: insets.top,
                    paddingBottom: insets.bottom,
                },
            ]}
        >
            <Olaunchpad />
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
        </Animated.View>
    )
}

const styles = createStyles(theme => ({
    container: {
        flex: 1,
        backgroundColor: theme.colors.black,
        padding: theme.gap(2),
        position: 'relative',
        paddingTop: StatusBar.currentHeight,
    },
    scrollView: {
        marginHorizontal: theme.gap(-2),
    },
    settingsContainer: {
        gap: theme.gap(4),
        paddingHorizontal: theme.gap(2),
    },
    spacer: {
        height: 32,
    },
    logo: {
        width: 80,
        height: 80,
        marginHorizontal: 'auto',
    },
}))
