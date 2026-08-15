package com.iti.mongez.designsystem.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.R
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * Helper function to extract initials from the topic/course name.
 * Example: "Data Structure" -> "DS", "Operating Systems" -> "OS", "Algorithms" -> "A"
 */
private fun getInitials(topic: String): String {
    return topic.trim()
        .split("\\s+".toRegex())
        .filter { it.isNotEmpty() }
        .map { it.first().uppercaseChar() }
        .take(2)
        .joinToString("")
}

@Composable
fun AppFocusCard(
    title: String,
    topic: String,
    duration: String,
    onStartClick: () -> Unit,
    modifier: Modifier = Modifier,
    imagePainter: Painter? = null
) {
    // Semantic mappings directly from your AppColorScheme tokens
    val contentOnBrandColor = Theme.colorScheme.brand.onPrimary
    val buttonBgColor = Theme.colorScheme.brand.onPrimary
    val buttonTextColor = Theme.colorScheme.brand.primaryVariant
    val badgeBgColor = Theme.colorScheme.brand.onPrimary.copy(alpha = 0.2f)
    val shadowColor = Theme.colorScheme.border.focused

    // Dynamic brand gradient using primary tokens across themes
    val cardGradient = Brush.linearGradient(
        colors = listOf(
            Theme.colorScheme.brand.primary,
            Theme.colorScheme.brand.primaryVariant
        ),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(204.dp)
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = shadowColor.copy(alpha = 0.4f),
                spotColor = shadowColor.copy(alpha = 0.4f)
            )
            .background(brush = cardGradient, shape = RoundedCornerShape(24.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Image / Initials Module
        if (imagePainter != null) {
            Image(
                painter = imagePainter,
                contentDescription = "$topic image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(160.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        } else {
            // Placeholder Box with translucent white background & white initials
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getInitials(topic),
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Right Content Stack
        Column(
            modifier = Modifier
                .width(139.dp)
                .height(180.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.Start
        ) {
            // Text Block (Title & Topic)
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.focus_card_lets_start),
                    color = contentOnBrandColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = topic,
                    color = contentOnBrandColor,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 30.sp
                )
            }

            // Duration Badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(badgeBgColor)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Duration",
                    tint = contentOnBrandColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = duration,
                    color = contentOnBrandColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // CTA Button
//            Box(
//                modifier = Modifier
//                    .width(127.dp)
//                    .height(36.dp)
//                    .shadow(
//                        elevation = 6.dp,
//                        shape = RoundedCornerShape(12.dp),
//                        spotColor = Color.Black.copy(alpha = 0.1f)
//                    )
//                    .clip(RoundedCornerShape(12.dp))
//                    .background(buttonBgColor)
//                    .clickable { onStartClick() },
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = "Let’s Start",
//                    color = buttonTextColor,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Medium,
//                    textAlign = TextAlign.Center
//                )
//            }
        }
    }
}

@Preview(showBackground = true, name = "Focus Card - Initials Fallback")
@Composable
private fun AppFocusCardInitialsPreview() {
    MongezTheme(darkTheme = false) {
        Box(modifier = Modifier.padding(16.dp)) {
            AppFocusCard(
                title = "Today's Focus",
                topic = "Data Structure",
                duration = "2h 15m",
                imagePainter = null,
                onStartClick = {}
            )
        }
    }
}