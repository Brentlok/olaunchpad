package expo.modules.launchpad

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.draw.clip

@Composable
fun LaunchpadRowItem(
    icon: ImageBitmap?,
    label: String,
    subLabel: String?,
    onClick: () -> Unit
) {
    val appColors = LocalAppColors.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .height(56.dp)
            .background(
                color = appColors.accentColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp)
    ) {
        if (icon != null) {
            Image(
                bitmap = icon,
                contentDescription = label,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
            text = label,
            color = colorResource(id = R.color.white)
        )
        if (subLabel != null) {
            Text(
                text = subLabel,
                color = colorResource(id = R.color.white),
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}
