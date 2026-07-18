package com.iti.mongez.designsystem.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * A highly reusable card component for displaying informational, AI, or alert content.
 *
 * @param header The title text (e.g., "AI Suggestion").
 * @param body The main description text.
 * @param icon The ImageVector icon to display on the right.
 * @param modifier Modifier for external layout adjustments.
 * @param tintColor The primary color for this specific card instance.
 */
@Composable
fun AppInfoCard(
    header: String,
    body: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    tintColor: Color = Theme.colorScheme.state.success
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Theme.colorScheme.surface.background)
            .border(
                width = 1.dp,
                color = tintColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Text Content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = header,
                color = tintColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                lineHeight = 16.sp
            )
            Text(
                text = body,
                color = Theme.colorScheme.text.primary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal,
                lineHeight = 14.sp
            )
        }

        // Icon Overlay
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(tintColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null, // Set null as this is decorative
                tint = tintColor,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

// Example of usage in your dashboard
@Preview(showBackground = true)
@Composable
private fun PreviewReusableCards() {
    MongezTheme {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Usage 1: AI Suggestion
            AppInfoCard(
                header = "AI Suggestion",
                body = "You're most productive around 7 PM. Start Networking before Algorithms today.",
                icon = androidx.compose.material.icons.Icons.Default.WbSunny
            )

            // Usage 2: Safety/Alert (Different Icon + Color)
            AppInfoCard(
                header = "Safety Alert",
                body = "Your password for the university portal hasn't been changed in 90 days.",
                icon = androidx.compose.material.icons.Icons.Default.Warning,
                tintColor = Theme.colorScheme.state.error
            )
        }
    }
}