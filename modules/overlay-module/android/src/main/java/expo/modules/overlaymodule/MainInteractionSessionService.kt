package expo.modules.overlaymodule

import android.os.Bundle
import android.service.voice.VoiceInteractionSession
import android.service.voice.VoiceInteractionSessionService

class MainInterationSessionService : VoiceInteractionSessionService() {
    override fun onNewSession(bundle: Bundle?): VoiceInteractionSession {
        return MainInteractionSession(this)
    }
}
