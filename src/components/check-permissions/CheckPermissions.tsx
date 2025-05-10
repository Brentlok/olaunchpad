import React, { useEffect, useState } from 'react'
import { APP_SETTINGS_NAMES } from '~/types'
import { CheckContactPermission } from './CheckContactPermission'
import { useStore } from '~/store'
import { OverlayModule } from 'overlay-module'
import { useWillBeActive } from '~/hooks'

export const CheckPermissions: React.FC = () => {
    const { isContactsEnabled } = useStore()
    const { willBeActive } = useWillBeActive()
    const [hasContactsPermission, setHasContactsPermission] = useState(() => OverlayModule.getHasReadContactsPermission())

    const withoutGrantedPermissions = APP_SETTINGS_NAMES.filter(setting => {
        switch (setting) {
            case 'isContactsEnabled':
                return isContactsEnabled && !hasContactsPermission
            default:
                return false
        }
    })

    useEffect(() => {
        if (!willBeActive) {
            return
        }

        setHasContactsPermission(OverlayModule.getHasReadContactsPermission())
    }, [willBeActive])

    return (
        <React.Fragment>
            {withoutGrantedPermissions.map(setting => {
                switch (setting) {
                    case 'isContactsEnabled':
                        return (
                            <CheckContactPermission
                                key={setting}
                                setHasContactsPermission={setHasContactsPermission}
                            />
                        )
                    default:
                        return null
                }
            })}
        </React.Fragment>
    )
}
