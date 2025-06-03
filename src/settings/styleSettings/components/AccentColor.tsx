import React, { useRef, useState } from 'react'
import { Pressable, View } from 'react-native'
import { ReduceMotion, runOnJS, useSharedValue, withTiming } from 'react-native-reanimated'
import ColorPicker, { ColorFormatsObject, colorKit, LuminanceCircular, Panel3, Preview } from 'reanimated-color-picker'
import { Button, Modal, Switch, Typography } from '~/components'
import { useTranslations } from '~/locale'
import { useStore } from '~/store'
import { colors, createStyles } from '~/style'

export const AccentColor = () => {
    const T = useTranslations()
    const [isColorPickerOpened, setIsColorPickerOpened] = useState(false)
    const { setAccentColor, setTextColor } = useStore()
    const initialAccentColor = useSharedValue(colors.accent.get())
    const initialTextColor = useSharedValue(colors.textColor.get())
    const [color, setColor] = useState(colors.accent.get())

    const onChangeColor = ({ hex, hsvaObj }: ColorFormatsObject) => {
        'worklet'

        colors.accent.set(hex)

        const compareColor = colors.textColor.get() === colors.black ? { h: 0, s: 0, v: 0 } : { h: 0, s: 0, v: 100 }
        const contrast = colorKit.runOnUI().contrastRatio(hsvaObj, compareColor)
        const reversedColor = colors.textColor.get() === colors.black ? colors.white : colors.black

        if (contrast < 4.5) {
            colors.textColor.set(reversedColor)
        }
    }

    const handleClose = () => {
        setIsColorPickerOpened(false)
        colors.accent.set(withTiming(initialAccentColor.value, { reduceMotion: ReduceMotion.Never }))
        colors.textColor.set(withTiming(initialTextColor.value, { reduceMotion: ReduceMotion.Never }))
        setColor(initialAccentColor.value)
    }

    const handleConfirm = () => {
        setIsColorPickerOpened(false)
        setColor(colors.accent.get())
        setAccentColor(colors.accent.get())
        setTextColor(colors.textColor.get())
    }

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
}))
