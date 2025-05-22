import React from 'react'
import Animated, { FadeIn, FadeOut, LinearTransition, ReduceMotion } from 'react-native-reanimated'
import { useIsMounted } from '~/hooks'
import { SearchSetting } from '~/types'
import { ExtraBrowser } from './ExtraBrowser'
import { ExtraYoutube } from './ExtraYoutube'

type ExtraSettingsProps = {
    setting: SearchSetting
    isEnabled: boolean
}

export const ExtraSettings: React.FunctionComponent<ExtraSettingsProps> = ({
    setting,
    isEnabled,
}) => {
    const isMounted = useIsMounted()
    const getExtraSetting = () => {
        switch (true) {
            case setting === 'isYoutubeEnabled' && isEnabled:
                return <ExtraYoutube />
            case setting === 'isBrowserEnabled' && isEnabled:
                return <ExtraBrowser />
            default:
                return null
        }
    }

    const extraSetting = getExtraSetting()

    if (extraSetting === null) {
        return null
    }

    return (
        <Animated.View
            entering={isMounted.get() ? FadeIn.reduceMotion(ReduceMotion.Never) : undefined}
            exiting={FadeOut.reduceMotion(ReduceMotion.Never)}
            layout={LinearTransition.reduceMotion(ReduceMotion.Never)}
        >
            {extraSetting}
        </Animated.View>
    )
}
