package expo.modules.overlaymodule

import android.content.Context
import android.os.Bundle
import android.service.voice.VoiceInteractionSession
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import expo.modules.overlaymodule.R

class MainInteractionSession(context: Context?) :
    VoiceInteractionSession(context) {

    private var sessionView: View? = null

    /**
     * Called when the session UI is going to be created.
     * This is where you inflate your layout for the session.
     */
    override fun onCreateContentView(): View? {
        val inflater = LayoutInflater.from(getContext())
        // Inflate your custom layout.
        // Make sure you have a 'session_overlay_layout.xml' in your res/layout folder.
        sessionView = inflater.inflate(R.layout.session_overlay_layout, null)

        // You can interact with views inside your layout here if needed
        // For example, to set text on a TextView:
        // val messageTextView = sessionView?.findViewById<TextView>(R.id.message_text_view)
        // messageTextView?.text = "Hello from Assistant!"

        return sessionView
    }

    override fun onShow(args: Bundle?, showFlags: Int) {
        super.onShow(args, showFlags)
        // This method is called when the session is shown.
        // The UI created in onCreateContentView() will be displayed.
        // You can update your UI here based on the 'args' Bundle or 'showFlags'.
        // For example:
        // args?.getString("greeting")?.let {
        //     val messageTextView = sessionView?.findViewById<TextView>(R.id.message_text_view)
        //     messageTextView?.text = it
        // }
    }

    override fun onDestroy() {
        // Clean up resources if needed
        sessionView = null
        super.onDestroy()
    }
}