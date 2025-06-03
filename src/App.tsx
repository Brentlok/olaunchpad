import { PortalProvider } from '@gorhom/portal'
import React, { useEffect, useState } from 'react'
import { SystemBars } from 'react-native-edge-to-edge'
import { GestureHandlerRootView } from 'react-native-gesture-handler'
import { configureReanimatedLogger, ReanimatedLogLevel } from 'react-native-reanimated'
import { SafeAreaProvider } from 'react-native-safe-area-context'
import { useWillBeActive } from '~/hooks'
import { Launchpad } from '~/modules'
import { Initial, Settings } from './screens'

configureReanimatedLogger({
    level: ReanimatedLogLevel.warn,
    strict: false,
})

export const App = () => {
    const [isDefaultAssistant, setIsDefaultAssistant] = useState(() => Launchpad.getIsDefaultAssistant())
    const { willBeActive } = useWillBeActive()

    useEffect(() => {
        if (!willBeActive) {
            return
        }

        setIsDefaultAssistant(Launchpad.getIsDefaultAssistant())
    }, [willBeActive])

    return (
        <SafeAreaProvider>
            <GestureHandlerRootView>
                <PortalProvider>
                    <SystemBars style="light" />
                    {isDefaultAssistant ? <Settings /> : <Initial />}
                </PortalProvider>
            </GestureHandlerRootView>
        </SafeAreaProvider>
    )
}
