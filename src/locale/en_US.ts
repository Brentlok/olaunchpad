import { SearchSetting } from '~/types'

export const en_US = {
    components: {
        setting: {
            isBrowserEnabled: 'Search in Browser',
            isYoutubeEnabled: 'Search in Youtube',
            isApplicationsEnabled: 'Search in Applications',
            isContactsEnabled: 'Search in Contacts',
            isPlayStoreEnabled: 'Search in Play Store',
            isCalculatorEnabled: 'Calculator',
        } satisfies Record<SearchSetting, string>,
        extraSettings: {
            youtube: {
                searchInBrowser: 'Search Youtube in browser',
            },
            browser: {
                title: 'Default Browser',
            },
        },
        styleSetting: {
            isBlurEnabled: 'Blur background',
        },
        checkPermissions: {
            contacts: {
                description: 'In order to use this feature, you must allow Olaunchpad to read your contacts.',
                grant: 'Grant Read Contacts Permission',
                grantButton: 'Grant',
                deny: 'Deny Read Contacts Permission and disable this feature',
                denyButton: 'Deny and disable',
            },
        },
    },
    screen: {
        initial: {
            title: 'You must set Olaunchpad as the default assistant in the system settings.',
            openSettings: 'Open Default Assistant Settings',
        },
        settings: {
            searchSettings: {
                title: 'Search Settings',
            },
            styleSettings: {
                title: 'Style Settings',
            },
            openLaunchpad: 'Open Launchpad',
        },
    },
} as const
