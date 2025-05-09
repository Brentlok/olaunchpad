import { NativeModule, requireNativeModule } from 'expo'

declare class OverlayModuleType extends NativeModule {
    hello(): string
    openAssistantSettings(): void
}

export const OverlayModule = requireNativeModule<OverlayModuleType>('OverlayModule')