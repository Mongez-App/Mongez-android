package com.iti.mongez.designsystem.screens.courses

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * A highly reusable dashed-border upload box component.
 * Adapts to both "Cover Image" and "Course Material" states.
 */
@Composable
fun AppUploadBox(
    title: String,
    primaryText: String,
    secondaryText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    iconContent: @Composable () -> Unit,
) {
    // Semantic mappings directly from AppColorScheme
    val titleColor = Theme.colorScheme.text.primary
    val subtitleColor = Theme.colorScheme.text.secondary
    val hintColor = Theme.colorScheme.text.hint

    // The background is a very faint tint. We use surfaceVariant as the base gray/tinted background token.
    val boxBackgroundColor = Theme.colorScheme.surface.surfaceVariant
    // primaryContainer maps to Purple100 (light) which perfectly mimics the #E0E7FF dashed border from CSS.
    val dashedBorderColor = Theme.colorScheme.brand.primaryContainer

    val iconBackgroundColor = Theme.colorScheme.surface.background
    val shadowColor = Theme.colorScheme.border.primary

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Label Section
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                color = titleColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            )

            if (subtitle != null) {
                Text(
                    text = subtitle,
                    color = subtitleColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 16.sp,
                    modifier = Modifier.padding(bottom = 1.dp) // Align baselines visually
                )
            }
        }

        // 2. Upload Box Area
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(172.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable { onClick() }
                .background(boxBackgroundColor)
                // Custom dashed border implementation
                .drawBehind {
                    drawRoundRect(
                        color = dashedBorderColor,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(20f, 20f), // Defines dash length and gap length
                                phase = 0f
                            )
                        ),
                        cornerRadius = CornerRadius(16.dp.toPx())
                    )
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Circular Icon Container
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .shadow(
                            elevation = 1.dp,
                            shape = CircleShape,
                            spotColor = shadowColor.copy(alpha = 0.05f),
                            ambientColor = shadowColor.copy(alpha = 0.05f)
                        )
                        .background(iconBackgroundColor, CircleShape)
                ) {
                    iconContent()
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Primary Text ("Upload cover image")
                Text(
                    text = primaryText,
                    color = titleColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Secondary Text ("PNG or JPG, up to 5 MB")
                Text(
                    text = secondaryText,
                    color = hintColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

// ──────────────────────────────────────────────────────────────
// Previews
// ──────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Upload Box - Thumbnail Mode (Light)")
@Composable
private fun AppUploadBoxThumbnailPreview() {
    MongezTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.background)
                .padding(24.dp)
        ) {
            AppUploadBox(
                title = "Thumbnail",
                primaryText = "Upload cover image",
                secondaryText = "PNG or JPG, up to 5 MB",
                onClick = {},
                iconContent = {
                    // Using an Emoji to match the provided Figma image (image_955408.png)
                    Text(text = "🖼️", fontSize = 24.sp)
                }
            )
        }
    }
}

@Preview(showBackground = true, name = "Upload Box - Material Mode (Dark)")
@Composable
private fun AppUploadBoxMaterialPreview() {
    MongezTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.background)
                .padding(24.dp)
        ) {
            AppUploadBox(
                title = "Course Material",
                subtitle = "(PDFs, slides, notes)",
                primaryText = "Upload material",
                secondaryText = "Tap to browse files",
                onClick = {},
                iconContent = {
                    // Simulating the plus icon matching the primary brand color (image_955424.png)
                    Text(
                        text = "+",
                        fontSize = 24.sp,
                        color = Theme.colorScheme.brand.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            )
        }
    }
}