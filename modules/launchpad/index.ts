import { NativeModule, requireNativeModule } from 'expo'

declare class LaunchpadType extends NativeModule {
    requestReadContactsPermission(): Promise<boolean>
    getHasReadContactsPermission(): boolean
    getIsDefaultAssistant(): boolean
}

export const Launchpad = requireNativeModule<LaunchpadType>('LaunchpadModule')
