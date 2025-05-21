import React from 'react'
import { SearchSetting } from '~/types'
import { ExtraYoutube } from './ExtraYoutube'
import { ExtraBrowser } from './ExtraBrowser'

type ExtraSettingsProps = {
    setting: SearchSetting,
    isEnabled: boolean
}

export const ExtraSettings: React.FunctionComponent<ExtraSettingsProps> = ({
    setting,
    isEnabled
}) => {
    switch (true) {
        case setting === 'isYoutubeEnabled' && isEnabled:
            return <ExtraYoutube />
        case setting === 'isBrowserEnabled' && isEnabled:
            return <ExtraBrowser />
        default:
            return null
    }
}