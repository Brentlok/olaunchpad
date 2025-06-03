import { makeMutable } from 'react-native-reanimated'
import { getState } from '~/store'

export const colors = {
    black: '#1A1A1D',
    gray: '#666666',
    dark: '#222831',
    accent: makeMutable(getState().accentColor),
    textColor: makeMutable(getState().textColor),
    white: '#EEEEEE',
}
