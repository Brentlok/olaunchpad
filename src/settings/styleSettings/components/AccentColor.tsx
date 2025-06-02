import React, { useRef, useState } from 'react'
import { Pressable, View } from 'react-native'
import { PressableStateCallbackType } from 'react-native-gesture-handler'
import { runOnJS } from 'react-native-reanimated'
import ColorPicker, { ColorFormatsObject, LuminanceCircular, Panel3, Preview } from 'reanimated-color-picker'
import { Button, Modal, Switch, Typography } from '~/components'
import { useTranslations } from '~/locale'
import { useStore } from '~/store'
import { colors, createStyles } from '~/style'

export const AccentColor = () => {
    const T = useTranslations()
    const [isColorPickerOpened, setIsColorPickerOpened] = useState(false)
    const timeoutRef = useRef<NodeJS.Timeout | null>(null)
    const { setAccentColor } = useStore()

    const updateAccentColorDebounced = (hex: string) => {
        if (timeoutRef.current) {
            clearTimeout(timeoutRef.current)
        }

        timeoutRef.current = setTimeout(() => {
            setAccentColor(hex)
        }, 300)
    }

    const onChangeColor = ({ hex }: ColorFormatsObject) => {
        'worklet'

        colors.accent.set(hex)

        runOnJS(updateAccentColorDebounced)(hex)
    }

    return (
        <React.Fragment>
            <View style={styles.settingContainer}>
                <Typography>
                    {T.components.styleSetting.accentColor}
                </Typography>
                <Pressable
                    style={styles.colorPreviewButton}
                    onPress={() => setIsColorPickerOpened(true)}
                >
                    <ColorPicker
                        style={styles.colorPreview}
                        value={colors.accent.get()}
                    >
                        <Preview hideInitialColor />
                    </ColorPicker>
                </Pressable>
            </View>
            <Modal
                isVisible={isColorPickerOpened}
                onClose={() => setIsColorPickerOpened(false)}
            >
                <ColorPicker
                    value={colors.accent.get()}
                    onChange={onChangeColor}
                >
                    <Preview style={styles.preview} />
                    <LuminanceCircular
                        style={styles.colorPanelCircular}
                        containerStyle={styles.colorPanelCircularContainer}
                    >
                        <Panel3 style={styles.colorPicker} />
                    </LuminanceCircular>
                </ColorPicker>
                <View style={styles.previewContainer}>
                    <Button>
                        {T.components.styleSetting.accentColorPreview}
                    </Button>
                    <Switch isEnabled />
                </View>
            </Modal>
        </React.Fragment>
    )
}

const styles = createStyles(theme => ({
    settingContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: theme.gap(2),
    },
    colorPreviewButton: {
        flex: 1,
    },
    colorPreview: {
        flex: 1,
    },
    colorPanelCircular: {
        height: 300,
    },
    colorPanelCircularContainer: {
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: theme.colors.dark,
        padding: theme.gap(2),
    },
    colorPicker: {
        flex: 1,
    },
    previewContainer: {
        marginTop: theme.gap(2),
        width: '100%',
        flexDirection: 'row',
        alignItems: 'center',
        gap: theme.gap(2),
    },
    preview: {
        marginBottom: theme.gap(2),
    },
}))
