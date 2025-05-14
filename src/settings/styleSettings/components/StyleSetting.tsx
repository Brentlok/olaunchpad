import { StyleSheet, View } from 'react-native'
import { useStore } from '~/store'
import { textUtils } from '~/utils'
import { useTranslations } from '~/locale'
import { StyleSetting as StyleSettingType } from '~/types'
import { Switch, Typography } from '~/components'

type StyleSettingProps = {
    setting: StyleSettingType
}

export const StyleSetting: React.FC<StyleSettingProps> = ({
    setting
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
        gap: 16
    }
})
