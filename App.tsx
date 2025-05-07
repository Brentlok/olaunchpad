import { Text, View } from 'react-native';
import { OverlayModule } from 'overlay-module'

export default function App() {
    return (
        <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
            <Text>{OverlayModule.hello()}</Text>
        </View>
    )
}