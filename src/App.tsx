import { PortalProvider } from '@gorhom/portal'
import React, { useEffect, useState } from 'react'
import { useWillBeActive } from '~/hooks'
import { Launchpad } from '~/modules'
import { Initial, Settings } from './screens'

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
