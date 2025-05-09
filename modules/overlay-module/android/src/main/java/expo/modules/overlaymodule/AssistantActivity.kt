package expo.modules.overlaymodule

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout

class AssistantActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setBackgroundDrawableResource(android.R.color.transparent)
        val params = window.attributes
        params.dimAmount = 0.5f
        window.attributes = params
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        // Enable blur behind for API 31+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            window.attributes.blurBehindRadius = 10
        }

        setContentView(R.layout.activity_assistant)

        val buttonYouTube = findViewById<Button>(R.id.button_youtube)
        val buttonGitHub = findViewById<Button>(R.id.button_github)
        val rootOverlay = findViewById<FrameLayout>(R.id.root_overlay)
        val buttonContainer = findViewById<LinearLayout>(R.id.button_container)

        buttonYouTube.setOnClickListener {
            openUrl("https://youtube.com")
        }

        buttonGitHub.setOnClickListener {
            openUrl("https://github.com")
        }

        // Dismiss overlay when tapping outside the button container
        rootOverlay.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                val location = IntArray(2)
                buttonContainer.getLocationOnScreen(location)
                val x = event.rawX.toInt()
                val y = event.rawY.toInt()
                val left = location[0]
                val top = location[1]
                val right = left + buttonContainer.width
                val bottom = top + buttonContainer.height

                if (x < left || x > right || y < top || y > bottom) {
                    finish()
                    true
                } else {
                    false
                }
            } else {
                false
            }
        }
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.kiwibrowser.browser")
        startActivity(intent)
        finish()
    }
}