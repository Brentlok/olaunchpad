package expo.modules.overlaymodule

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class AssistProxyActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
        val intent = Intent(this, MainInteractionService::class.java)
        intent.action = Intent.ACTION_ASSIST
        startService(intent)
    }
}
