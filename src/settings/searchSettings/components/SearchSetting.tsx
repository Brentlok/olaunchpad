import { StyleSheet, View } from 'react-native'
import { useStore } from '~/store'
import { textUtils } from '~/utils'
import { useTranslations } from '~/locale'
import { SearchSetting as SearchSettingType } from '~/types'
import { Switch } from '~/components'
import { Typography } from '~/components'

type SearchSettingProps = {
    setting: SearchSettingType
}

export const SearchSetting: React.FC<SearchSettingProps> = ({
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
                {T.components.setting[setting]}
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
