import { StyleSheet, View } from 'react-native'
import { Switch, Typography } from '~/components'
import { useTranslations } from '~/locale'
import { useStore } from '~/store'
import { StyleSetting as StyleSettingType } from '~/types'
import { textUtils } from '~/utils'

type StyleSettingProps = {
    setting: StyleSettingType
}

export const StyleSetting: React.FC<StyleSettingProps> = ({
    setting,
}) => {
    const T = useTranslations()
    const store = useStore()
    const isEnabled = store[setting]
    const actionKey = `set${textUtils.capitalize(setting)}` as const
    const onChange = store[actionKey]

    return (
        <View style={styles.settingContainer}>
            <Typography>
                {T.components.styleSetting[setting]}
            </Typography>
            <Switch
                isEnabled={isEnabled}
                onChange={onChange}
            />
        </View>
    )
}

const styles = StyleSheet.create({
    settingContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: 16,
    },
})
