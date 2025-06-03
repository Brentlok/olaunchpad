import React, { useEffect, useState } from 'react'
import { Pressable, View } from 'react-native'
import { ReduceMotion, useSharedValue, withTiming } from 'react-native-reanimated'
import ColorPicker, { ColorFormatsObject, colorKit, LuminanceCircular, Panel3, Preview } from 'reanimated-color-picker'
import { Button, Modal, Switch, Typography } from '~/components'
import { useTranslations } from '~/locale'
import { useStore } from '~/store'
import { colors, createStyles } from '~/style'
import { colorUtils } from '~/utils'

export const AccentColor = () => {
    const T = useTranslations()
    const [isColorPickerOpened, setIsColorPickerOpened] = useState(false)
    const { setAccentColor, setTextColor, dynamicTextColor, setDynamicTextColor } = useStore()
    const initialAccentColor = useSharedValue(colors.accent.get())
    const initialTextColor = useSharedValue(colors.textColor.get())
    const textColor = useSharedValue(colors.textColor.get())
    const [color, setColor] = useState(colors.accent.get())

    const onChangeColor = ({ hex }: ColorFormatsObject) => {
        'worklet'

        colors.accent.set(hex)

        if (!dynamicTextColor) {
            return
        }

        const { contrast, reversedColor } = colorUtils.getColorsContrast(hex, textColor.value)

        if (contrast < 4.5) {
            textColor.set(reversedColor)
            colors.textColor.set(withTiming(reversedColor, { reduceMotion: ReduceMotion.Never }))
        }
    }

    const handleClose = () => {
        setIsColorPickerOpened(false)
        colors.accent.set(withTiming(initialAccentColor.value, { reduceMotion: ReduceMotion.Never }))
        colors.textColor.set(withTiming(initialTextColor.value, { reduceMotion: ReduceMotion.Never }))
        setColor(initialAccentColor.value)
        setTextColor(initialTextColor.value)
    }

    const handleConfirm = () => {
        setIsColorPickerOpened(false)
        setColor(colors.accent.value)
        setAccentColor(colors.accent.value)
        setTextColor(colorKit.HEX(colors.textColor.value))
        textColor.set(colors.textColor.value)
        initialTextColor.set(colors.textColor.value)
        initialAccentColor.set(colors.accent.value)
    }

    useEffect(() => {
        if (!dynamicTextColor) {
            textColor.set(colors.white)
            colors.textColor.set(withTiming(colors.white, { reduceMotion: ReduceMotion.Never }))
            setTextColor(colors.white)
            initialTextColor.set(colors.white)

            return
        }

        const { contrast, reversedColor } = colorUtils.getColorsContrast(colors.accent.get(), textColor.get())

        if (contrast < 4.5) {
            setTextColor(reversedColor)
            textColor.set(reversedColor)
            colors.textColor.set(withTiming(reversedColor, { reduceMotion: ReduceMotion.Never }))
            initialTextColor.set(reversedColor)
        }
    }, [dynamicTextColor])

    return (
        <React.Fragment>
            <View style={styles.settingContainer}>
                <Typography>
                    {T.components.styleSetting.changeAccentColor}
                </Typography>
                <Pressable
                    style={styles.colorPreviewButton}
                    onPress={() => setIsColorPickerOpened(true)}
                >
                    <ColorPicker
                        style={styles.colorPreview}
                        value={color}
                    >
                        <Preview hideInitialColor />
                    </ColorPicker>
                </Pressable>
            </View>
            <Modal
                isVisible={isColorPickerOpened}
                onClose={handleClose}
            >
                <View style={styles.modalContainer}>
                    <ColorPicker
                        value={color}
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
                    <View style={styles.switchContainer}>
                        <Typography>
                            {T.components.styleSetting.dynamicTextColor}
                        </Typography>
                        <Switch
                            isEnabled={dynamicTextColor}
                            onChange={setDynamicTextColor}
                        />
                    </View>
                    <View>
                        <Button onPress={handleConfirm}>
                            {T.components.styleSetting.accentColorConfirm}
                        </Button>
                    </View>
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
    modalContainer: {
        gap: theme.gap(2),
    },
    switchContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: theme.gap(2),
    },
}))
