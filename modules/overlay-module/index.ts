import { NativeModule, requireNativeModule } from 'expo'

declare class OverlayModuleType extends NativeModule {
    requestReadContactsPermission(): Promise<boolean>
    getHasReadContactsPermission(): boolean
    getIsDefaultAssistant(): boolean
    openAssistantSettings(): void
}

export const OverlayModule = requireNativeModule<OverlayModuleType>('OverlayModule')
