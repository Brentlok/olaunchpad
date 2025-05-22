import React, { useEffect, useState } from 'react'
import { useWillBeActive } from '~/hooks'
import { Launchpad } from '~/modules'
import { useStore } from '~/store'
import { SEARCH_SETTINGS_NAMES } from '~/types'
import { CheckContactPermission } from './CheckContactPermission'

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
