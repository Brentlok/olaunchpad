import { StyleSheet, Text, View } from 'react-native'
import { useStore } from '~/store'
import { textUtils } from '~/utils'
import { useTranslations } from '~/locale'
import { AppSetting } from '~/types'
import { Switch } from './Switch'

type SettingProps = {
    setting: AppSetting
}

export const Setting: React.FC<SettingProps> = ({
    setting
}) => {
    const T = useTranslations()
    const store = useStore()
    const isEnabled = store[setting]
    const actionKey = `set${textUtils.capitalize(setting)}` as const
    const onChange = store[actionKey]

    return (
        <View style={styles.settingContainer}>
            <Text style={styles.settingText}>
                {T.components.setting[setting]}
            </Text>
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
        gap: 16
    },
    settingText: {
        fontSize: 16,
        color: 'white'
    }
})
