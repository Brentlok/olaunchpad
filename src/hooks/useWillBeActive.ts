import { useEffect, useState } from 'react'
import { AppState } from 'react-native'

export const useWillBeActive = () => {
    const [willBeActive, setWillBeActive] = useState(false)

    useEffect(() => {
        const focusListener = AppState.addEventListener('focus', () => {
            setWillBeActive(true)
        })

        const blurListener = AppState.addEventListener('blur', () => {
            setWillBeActive(false)
        })

        return () => {
            focusListener.remove()
            blurListener.remove()
        }
    }, [])

    return {
        willBeActive,
    }
}
