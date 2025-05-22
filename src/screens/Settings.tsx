import React from 'react'
import { Dimensions, Image, ScrollView, StatusBar, StyleSheet, View } from 'react-native'
import Animated, { SlideInRight, SlideOutRight } from 'react-native-reanimated'
import { Images } from '~/assets'
import { Button, CheckPermissions } from '~/components'
import { useTranslations } from '~/locale'
import { Launchpad } from '~/modules'
import { SearchSettings, StyleSettings } from '~/settings'
import { colors } from '~/style'

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

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.black,
        padding: 16,
        position: 'relative',
        paddingTop: StatusBar.currentHeight,
        paddingBottom: Dimensions.get('screen').height - Dimensions.get('window').height - (StatusBar.currentHeight ?? 0),
    },
    scrollView: {
        marginHorizontal: -16,
    },
    settingsContainer: {
        gap: 16,
        paddingHorizontal: 16,
    },
    spacer: {
        height: 32,
    },
    logo: {
        width: 80,
        height: 80,
        marginHorizontal: 'auto',
    },
})
