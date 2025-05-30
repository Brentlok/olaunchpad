import { useEffect } from 'react'
import { BackHandler } from 'react-native'

export const useBackHandler = (handler: () => boolean) => {
    useEffect(() => {
        const subscribe = BackHandler.addEventListener('hardwareBackPress', handler)

        return () => subscribe.remove()
    }, [handler])
}
