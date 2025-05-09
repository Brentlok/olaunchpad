package expo.modules.overlaymodule

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import android.content.pm.PackageManager
import androidx.core.graphics.createBitmap
import androidx.core.net.toUri

fun drawableToImageBitmap(drawable: Drawable): ImageBitmap {
    val bitmap = createBitmap(
        drawable.intrinsicWidth.takeIf { it > 0 } ?: 1,
        drawable.intrinsicHeight.takeIf { it > 0 } ?: 1,
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap.asImageBitmap()
}

fun getInstalledApps(context: Context, query: String): List<ResolveInfo> {
    val pm = context.packageManager
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.addCategory(Intent.CATEGORY_LAUNCHER)
    val apps = pm.queryIntentActivities(intent, 0)
    return apps.filter {
        val label = it.loadLabel(pm).toString()
        label.contains(query, ignoreCase = true)
    }.take(3)
}

fun openUrl(context: Context, url: String) {
    val pm: PackageManager = context.packageManager
    val kiwiPackage = "com.kiwibrowser.browser"
    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    val isKiwiInstalled = try {
        pm.getPackageInfo(kiwiPackage, 0)
        true
    } catch (e: Exception) {
        false
    }

    if (isKiwiInstalled) {
        intent.setPackage(kiwiPackage)
    }

    context.startActivity(intent)
}