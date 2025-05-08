package expo.modules.overlaymodule

import android.service.voice.VoiceInteractionService
import android.util.Log

class MainInteractionService : VoiceInteractionService() {
    companion object {
        const val TAG = "MainInteractionService"
    }

    override fun onReady() {
        Log.i(TAG, "onReady called")
        super.onReady()
        Log.i(TAG, "Creating $this")
    }
}