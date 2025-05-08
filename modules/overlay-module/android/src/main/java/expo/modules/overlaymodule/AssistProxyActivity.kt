package expo.modules.overlaymodule

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

class AssistProxyActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("AssistProxyActivity", "onCreate called")
        super.onCreate(savedInstanceState)
        finish()
        val intent = Intent(this, MainInteractionService::class.java)
        intent.action = Intent.ACTION_ASSIST
        startService(intent)
    }
}