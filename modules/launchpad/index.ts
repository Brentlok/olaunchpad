import { NativeModule, requireNativeModule } from 'expo'
import { Browser } from '~/types'

declare class LaunchpadType extends NativeModule {
    requestReadContactsPermission(): Promise<boolean>
    getHasReadContactsPermission(): boolean
    getIsDefaultAssistant(): boolean
    getDefaultBrowser(): string
    getAllBrowsers(): Array<Browser>
    open(): void
}

export const Launchpad = requireNativeModule<LaunchpadType>('LaunchpadModule')
