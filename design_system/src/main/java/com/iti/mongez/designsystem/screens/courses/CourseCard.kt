package com.iti.mongez.designsystem.screens.courses

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

/**
 * A reusable course card component displaying a course thumbnail, title, and progress.
 * The progress bar color dynamically adapts based on the completion percentage.
 */
@Composable
fun CourseCard(
    title: String,
    progress: Float,
    imagePainter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = Theme.spacing
    val radius = Theme.radius
    val elevation = Theme.elevation

    val backgroundColor = Theme.colorScheme.card.background
    val borderColor = Theme.colorScheme.card.border
    val titleColor = Theme.colorScheme.text.primary
    val hintColor = Theme.colorScheme.text.hint
    val trackColor = Theme.colorScheme.surface.surfaceContainer
    val shadowColor = Theme.colorScheme.border.disabled

    val dynamicProgressColor = when {
        progress >= 0.5f -> Theme.colorScheme.state.success
        progress >= 0.3f -> Theme.colorScheme.brand.primary
        else -> Theme.colorScheme.state.warning
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = elevation.md,
                shape = RoundedCornerShape(radius.dialog),
                spotColor = shadowColor.copy(alpha = 0.05f),
                ambientColor = shadowColor.copy(alpha = 0.05f)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(radius.dialog)
            )
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(radius.dialog)
            )
            .clip(RoundedCornerShape(radius.dialog))
            .clickable(onClick = onClick)
            .padding(spacing.lg),
        horizontalArrangement = Arrangement.spacedBy(spacing.lg),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = imagePainter,
            contentDescription = "$title thumbnail",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(width = 128.dp, height = 144.dp)
                .clip(RoundedCornerShape(radius.lg))
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .height(144.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.xs)
            ) {
                Text(
                    text = title,
                    color = titleColor,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Progress",
                    color = hintColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.xs)
            ) {
                val percentageText = "${(progress * 100).toInt()}%"

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        text = percentageText,
                        color = dynamicProgressColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = percentageText,
                        color = hintColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(trackColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.coerceIn(0f, 1f))
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(dynamicProgressColor)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Course Card - High Progress (Light)")
@Composable
private fun CourseCardHighProgressPreview() {
    MongezTheme(darkTheme = false) {
        Box(
            modifier = Modifier.padding(Theme.spacing.xl)
        ) {
            CourseCard(
                title = "Operating Systems",
                progress = 0.1f,
                imagePainter = ColorPainter(Color(0xFF0F172A)),
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true, name = "Course Card - Low Progress (Dark)")
@Composable
private fun CourseCardLowProgressPreview() {
    MongezTheme(darkTheme = true) {
        Box(
            modifier = Modifier
                .padding(Theme.spacing.xl)
                .background(Color(0xFF111317))
        ) {
            CourseCard(
                title = "Algorithms",
                progress = 0.34f,
                imagePainter = ColorPainter(Color(0xFF1E293B)),
                onClick = {}
            )
        }
    }
}