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
    val titleColor = Theme.colorScheme.text.primary
    val subtitleColor = Theme.colorScheme.text.secondary
    val hintColor = Theme.colorScheme.text.hint

    val boxBackgroundColor = Theme.colorScheme.surface.surfaceVariant
    val dashedBorderColor = Theme.colorScheme.border.focused

    val iconBackgroundColor = Theme.colorScheme.surface.background
    val shadowColor = Theme.colorScheme.border.primary
    val cornerRadius = Theme.radius.lg
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.sm)
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.xs)
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
                    modifier = Modifier.padding(bottom = 1.dp)
                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(172.dp)
                .clip(RoundedCornerShape(Theme.radius.lg))
                .clickable(onClick = onClick)
                .background(boxBackgroundColor)
                .drawBehind {
                    drawRoundRect(
                        color = dashedBorderColor,
                        style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(20f, 20f),
                                phase = 0f
                            )
                        ),
                        cornerRadius = CornerRadius(cornerRadius.toPx())
                    )
                }
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(Theme.spacing.huge)
                        .shadow(
                            elevation = Theme.elevation.xs,
                            shape = CircleShape,
                            spotColor = shadowColor.copy(alpha = 0.05f),
                            ambientColor = shadowColor.copy(alpha = 0.05f)
                        )
                        .background(iconBackgroundColor, CircleShape)
                ) {
                    iconContent()
                }

                Spacer(modifier = Modifier.height(Theme.spacing.lg))

                Text(
                    text = primaryText,
                    color = titleColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(Theme.spacing.xs))

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

@Preview(showBackground = true, name = "Upload Box - Thumbnail Mode (Light)")
@Composable
private fun AppUploadBoxThumbnailPreview() {
    MongezTheme(darkTheme = false) {
        Box(
            modifier = Modifier
                .background(Theme.colorScheme.surface.background)
                .padding(Theme.spacing.xl)
        ) {
            AppUploadBox(
                title = "Thumbnail",
                primaryText = "Upload cover image",
                secondaryText = "PNG or JPG, up to 5 MB",
                onClick = {},
                iconContent = {
                    Text(
                        text = "🖼️",
                        fontSize = 24.sp
                    )
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
                .padding(Theme.spacing.xl)
        ) {
            AppUploadBox(
                title = "Course Material",
                subtitle = "(PDFs, slides, notes)",
                primaryText = "Upload material",
                secondaryText = "Tap to browse files",
                onClick = {},
                iconContent = {
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