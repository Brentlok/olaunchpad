import React from 'react'
import { Dimensions, Image, ScrollView, StatusBar, View } from 'react-native'
import Animated, { SlideInRight, SlideOutRight } from 'react-native-reanimated'
import { Images } from '~/assets'
import { Button, CheckPermissions } from '~/components'
import { useTranslations } from '~/locale'
import { Launchpad } from '~/modules'
import { SearchSettings, StyleSettings } from '~/settings'
import { colors, createStyles } from '~/style'

export const Settings = () => {
    const T = useTranslations()

    return (
        <Animated.View
            entering={SlideInRight}
            exiting={SlideOutRight}
            style={styles.container}
        >
            <Image
                source={Images.logo}
                style={styles.logo}
            />
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
        paddingBottom: Dimensions.get('screen').height - Dimensions.get('window').height - (StatusBar.currentHeight ?? 0),
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
