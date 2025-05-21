import React, { useEffect, useState } from 'react'
import { Launchpad } from '~/modules'
import { useWillBeActive } from '~/hooks'
import { Initial, Settings } from './screens'
import { PortalProvider } from '@gorhom/portal'

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
        <PortalProvider>
            {isDefaultAssistant ? <Settings /> : <Initial />}
        </PortalProvider>
    )
}
