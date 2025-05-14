import React, { useEffect, useState } from 'react'
import { SEARCH_SETTINGS_NAMES } from '~/types'
import { CheckContactPermission } from './CheckContactPermission'
import { useStore } from '~/store'
import { Launchpad } from '~/modules'
import { useWillBeActive } from '~/hooks'

export const CheckPermissions: React.FC = () => {
    const { isContactsEnabled } = useStore()
    const { willBeActive } = useWillBeActive()
    const [hasContactsPermission, setHasContactsPermission] = useState(() => Launchpad.getHasReadContactsPermission())

    const withoutGrantedPermissions = SEARCH_SETTINGS_NAMES.filter(setting => {
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

        setHasContactsPermission(Launchpad.getHasReadContactsPermission())
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
