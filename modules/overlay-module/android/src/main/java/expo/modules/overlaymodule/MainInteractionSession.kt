package expo.modules.overlaymodule

import android.content.Context
import android.os.Bundle
import android.service.voice.VoiceInteractionSession
import android.util.Log
import android.app.assist.AssistContent
import android.app.assist.AssistStructure

class MainInteractionSession(context: Context?) :
    VoiceInteractionSession(context) {

    private val TAG = "MainInteractionSession"

    init {
        Log.d(TAG, "Session Initialized")
    }

    override fun onShow(args: Bundle?, showFlags: Int) {
        super.onShow(args, showFlags)
        Log.d(TAG, "onShow called. Args: $args, Flags: $showFlags")
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy called")
        super.onDestroy()
    }

    override fun onHide() {
        super.onHide()
        Log.d(TAG, "onHide called")
    }

    override fun onHandleAssist(data: Bundle?, structure: AssistStructure?, content: AssistContent?) {
        super.onHandleAssist(data, structure, content)
        Log.d(TAG, "onHandleAssist called")
    }
}