import { StyleSheet, View } from "react-native"
import { Typography } from "~/components"
import { useTranslations } from "~/locale"
import { StyleSetting } from "./components"

export const StyleSettings = () => {
    const T = useTranslations()

    return (
        <View style={styles.container}>
            <Typography
                variant="header"
                center
            >
                {T.screen.settings.styleSettings.title}
            </Typography>
            <StyleSetting setting="isBlurEnabled" />
        </View>
    )
}

const styles = StyleSheet.create({
    container: {
        gap: 16
    }
})