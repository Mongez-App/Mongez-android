package com.iti.mongez.designsystem.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * A reusable glowing action button matching the Figma specifications.
 * Features a 48dp height, 16dp rounded corners, purple glow shadow, and 12dp spacing between icon and text.
 */
@Composable
fun AppGlowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = Icons.Default.Add,
    enabled: Boolean = true,
) {
    val shape = RoundedCornerShape(16.dp)
    val brandPrimary = Theme.colorScheme.brand.primary
    val backgroundColor = Theme.colorScheme.surface.surfaceVariant

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            // Figma: box-shadow: 0px 0px 7px rgba(90, 103, 216, 0.6)
            .shadow(
                elevation = Theme.elevation.xl,
                shape = shape,
                spotColor = brandPrimary.copy(alpha = 1f),
                ambientColor = brandPrimary.copy(alpha = 0.9f)
            )
            .clip(shape)
            .background(if (enabled) backgroundColor else Theme.colorScheme.button.disabledBackground)
            .then(
                if (enabled) Modifier.clickable(onClick = onClick) else Modifier
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md) // Figma: gap: 12px
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (enabled) brandPrimary else Theme.colorScheme.button.disabledContent,
                    modifier = Modifier.size(16.dp) // Figma: 16px x 16px
                )
            }

            Text(
                text = text,
                color = if (enabled) brandPrimary else Theme.colorScheme.button.disabledContent,
                fontSize = 15.sp, // Figma: font-size: 15px
                fontWeight = FontWeight.SemiBold // Figma: font-weight: 600
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────
// Previews
// ──────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Glow Button - Light Mode")
@Composable
private fun AppGlowButtonLightPreview() {
    MongezTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.background)
                .padding(Theme.spacing.lg)
        ) {
            AppGlowButton(
                text = "Upload Material",
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Glow Button - Dark Mode")
@Composable
private fun AppGlowButtonDarkPreview() {
    MongezTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.background)
                .padding(Theme.spacing.lg)
        ) {
            AppGlowButton(
                text = "Upload Material",
                onClick = {}
            )
        }
    }
}