import React, { useEffect, useState } from 'react'
import { OverlayModule } from 'overlay-module'
import { useWillBeActive } from '~/hooks'
import { Initial, Settings } from './screens'

export const App = () => {
    const [isDefaultAssistant, setIsDefaultAssistant] = useState(() => OverlayModule.getIsDefaultAssistant())
    const { willBeActive } = useWillBeActive()

    useEffect(() => {
        if (!willBeActive) {
            return
        }

        setIsDefaultAssistant(OverlayModule.getIsDefaultAssistant())
    }, [willBeActive])

    return isDefaultAssistant ? <Settings /> : <Initial />
}
