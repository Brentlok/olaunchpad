import { NativeModule, requireNativeModule } from 'expo'

declare class OverlayModuleType extends NativeModule {
    requestReadContactsPermission(): Promise<boolean>
    getHasReadContactsPermission(): boolean
    getIsDefaultAssistant(): boolean
}

export const OverlayModule = requireNativeModule<OverlayModuleType>('OverlayModule')
