import Animated, { LinearTransition, ReduceMotion } from 'react-native-reanimated'
import { Switch } from '~/components'
import { Typography } from '~/components'
import { useTranslations } from '~/locale'
import { useStore } from '~/store'
import { createStyles } from '~/style'
import { SearchSetting as SearchSettingType } from '~/types'
import { textUtils } from '~/utils'
import { ExtraSettings } from './extra-settings'

type SearchSettingProps = {
    setting: SearchSettingType
}

export const SearchSetting: React.FC<SearchSettingProps> = ({
    setting,
}) => {
    const T = useTranslations()
    const store = useStore()
    const isEnabled = store[setting]
    const actionKey = `set${textUtils.capitalize(setting)}` as const
    const onChange = store[actionKey]

    return (
        <>
            <Animated.View
                layout={LinearTransition.reduceMotion(ReduceMotion.Never)}
                style={styles.settingContainer}
            >
                <Typography>
                    {T.components.setting[setting]}
                </Typography>
                <Switch
                    isEnabled={isEnabled}
                    onChange={onChange}
                />
            </Animated.View>
            <ExtraSettings
                setting={setting}
                isEnabled={isEnabled}
            />
        </>
    )
}

const styles = createStyles(theme => ({
    settingContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        gap: theme.gap(2),
    },
}))
