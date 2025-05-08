package expo.modules.overlaymodule

import android.os.Bundle
import android.service.voice.VoiceInteractionSession
import android.service.voice.VoiceInteractionSessionService
import android.util.Log

class MainInteractionSessionService : VoiceInteractionSessionService() {

    override fun onNewSession(args: Bundle?): VoiceInteractionSession {
        Log.d("MainInteractionSessionService", "onNewSession called")
        return MainInteractionSession(this)
    }
}
