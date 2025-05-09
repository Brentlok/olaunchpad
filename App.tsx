import { StyleSheet, Text, View } from 'react-native';
import { OverlayModule } from 'overlay-module'
import { Button } from './components';
import { colors } from './colors';

export const App = () => {
    return (
        <View style={styles.container}>
            <Text style={styles.header}>
                You must set Olaunchpad as the default assistant in the system settings.
            </Text>
            <Button onPress={() => OverlayModule.openAssistantSettings()}>
                Open Default Assistant Settings
            </Button>
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.black,
        padding: 16,
        justifyContent: 'center',
        gap: 32
    },
    header: {
        color: colors.white,
        fontSize: 24,
        textAlign: 'center',
    }
})
