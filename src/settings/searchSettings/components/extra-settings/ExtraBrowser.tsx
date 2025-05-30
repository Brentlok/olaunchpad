import WheelPicker, { PickerItem } from '@quidone/react-native-wheel-picker'
import React from 'react'
import { useMemo, useState } from 'react'
import { Image, Pressable, View } from 'react-native'
import { Typography } from '~/components'
import { Modal } from '~/components/Modal'
import { useTranslations } from '~/locale'
import { Launchpad } from '~/modules'
import { useStore } from '~/store'
import { colors, createStyles } from '~/style'

export const ExtraBrowser = () => {
    const T = useTranslations()
    const { defaultBrowser, setDefaultBrowser } = useStore()
    const browsers = useMemo(() => Launchpad.getAllBrowsers(), [])
    const iconsByBrowsers = useMemo(() => Object.fromEntries(browsers.map(browser => [browser.packageName, browser.icon])), [])
    const [isPickerOpened, setIsPickerOpened] = useState(false)

    const browsersList = browsers.map(browser => ({
        value: browser.packageName,
        label: browser.label,
    } satisfies PickerItem<string>))

    return (
        <View style={styles.container}>
            <Typography>
                {T.components.extraSettings.browser.title}
            </Typography>
            <Image
                source={{ uri: `data:image/png;base64,${iconsByBrowsers[defaultBrowser]}` }}
                style={styles.iconSmall}
            />
            <Pressable
                onPress={() => setIsPickerOpened(true)}
                style={state => [styles.change, { opacity: state.pressed ? 0.8 : 1 }]}
            >
                <Typography variant="tiny">
                    Change default browser
                </Typography>
            </Pressable>
            <Modal
                isVisible={isPickerOpened}
                onClose={() => setIsPickerOpened(false)}
            >
                <Typography variant="header">
                    {T.components.extraSettings.browser.title}
                </Typography>
                <WheelPicker
                    data={browsersList}
                    value={defaultBrowser}
                    onValueChanged={({ item }) => setDefaultBrowser(item.value)}
                    itemHeight={50}
                    visibleItemCount={3}
                    renderItem={({ item }) => {
                        const icon = iconsByBrowsers[item.value]

                        return (
                            <View style={styles.item}>
                                <Image
                                    source={{ uri: `data:image/png;base64,${icon}` }}
                                    style={styles.icon}
                                />
                                <Typography variant="header">
                                    {item.label}
                                </Typography>
                            </View>
                        )
                    }}
                />
            </Modal>
        </View>
    )
}

const styles = createStyles(theme => ({
    container: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: theme.gap(1),
    },
    item: {
        height: 50,
        flexDirection: 'row',
        alignItems: 'center',
        gap: theme.gap(1),
    },
    iconSmall: {
        width: 24,
        height: 24,
    },
    icon: {
        width: 40,
        height: 40,
    },
    change: {
        borderWidth: 1,
        borderColor: colors.white,
        padding: theme.gap(1),
        borderRadius: theme.gap(1),
        marginLeft: 'auto',
    },
}))
