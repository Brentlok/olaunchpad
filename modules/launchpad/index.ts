import { NativeModule, requireNativeModule } from 'expo'

declare class LaunchpadType extends NativeModule {
    requestReadContactsPermission(): Promise<boolean>
    getHasReadContactsPermission(): boolean
    getIsDefaultAssistant(): boolean
    open(): void
}

export const Launchpad = requireNativeModule<LaunchpadType>('LaunchpadModule')
