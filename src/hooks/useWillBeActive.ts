import { useEffect, useState } from 'react'
import { AppState } from 'react-native'

export const useWillBeActive = () => {
    const [willBeActive, setWillBeActive] = useState(false)

    useEffect(() => {
        const listener = AppState.addEventListener('change', (nextState: string) => {
            setWillBeActive(nextState === 'active')
        })

        return () => listener.remove()
    }, [])

    return {
        willBeActive,
    }
}
