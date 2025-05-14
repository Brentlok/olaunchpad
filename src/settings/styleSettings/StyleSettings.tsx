import { Typography } from "~/components"
import { useTranslations } from "~/locale"
import { StyleSetting } from "./components"

export const StyleSettings = () => {
    const T = useTranslations()

    return (
        <>
            <Typography
                variant="header"
                center
            >
                {T.screen.settings.styleSettings.title}
            </Typography>
            <StyleSetting setting="isBlurEnabled" />
        </>
    )
}