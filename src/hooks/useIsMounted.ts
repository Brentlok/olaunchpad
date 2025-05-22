import { useEffect } from 'react'
import { useSharedValue } from 'react-native-reanimated'

export const useIsMounted = () => {
    const isMounted = useSharedValue(false)

    useEffect(() => {
        Promise.resolve().then(() => isMounted.set(true))
    }, [])

    return isMounted
}
