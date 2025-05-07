import { NativeModule, requireNativeModule } from 'expo'

declare class OverlayModuleType extends NativeModule {
    hello(): string
}

export const OverlayModule = requireNativeModule<OverlayModuleType>('OverlayModule')
