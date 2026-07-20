package com.iti.mongez.presentation.preferences.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun DaysSelectionGrid(
    selectedDays: Set<String>,
    onDayToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridItems = listOf(
        "Sun", "Mon", "Tue",
        "Wed", "Thu", "",
        "", "Sat", "Fri"
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.md),
        contentPadding = PaddingValues(vertical = Theme.spacing.lg),
        userScrollEnabled = false
    ) {
        items(gridItems.size) { index ->
            val day = gridItems[index]
            if (day.isNotEmpty()) {
                DayItem(
                    label = day,
                    isSelected = selectedDays.contains(day),
                    onToggle = { onDayToggle(day) }
                )
            } else {
                Spacer(modifier = Modifier.aspectRatio(1f))
            }
        }
    }
}

@Composable
private fun DayItem(
    label: String,
    isSelected: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shadowColor = Theme.colorScheme.brand.primary.copy(alpha = 0.7f)
    val cornerRadius = Theme.radius.lg

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(Theme.spacing.xs)
                .drawBehind {
                    drawIntoCanvas { canvas ->
                        val paint = Paint()
                        val frameworkPaint = paint.asFrameworkPaint()
                        val blurRadiusPx = 10.dp.toPx()

                        frameworkPaint.color = Color.Transparent.toArgb()
                        frameworkPaint.setShadowLayer(
                            blurRadiusPx,
                            0f,
                            0f,
                            shadowColor.toArgb()
                        )
                        
                        val radiusPx = cornerRadius.toPx()
                        canvas.drawRoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width,
                            bottom = size.height,
                            radiusX = radiusPx,
                            radiusY = radiusPx,
                            paint = paint
                        )
                    }
                }
                .background(
                    if (isSelected) Theme.colorScheme.brand.primary else Color.White,
                    RoundedCornerShape(cornerRadius)
                )
                .border(
                    if (isSelected) 0.dp else 1.dp,
                    if (isSelected) Color.Transparent else Theme.colorScheme.brand.primary.copy(alpha = 0.1f),
                    RoundedCornerShape(cornerRadius)
                )
                .clip(RoundedCornerShape(cornerRadius))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onToggle() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = Theme.typography.title.medium,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.White else Theme.colorScheme.text.primary
            )
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .background(Color.White, CircleShape)
                    .border(2.dp, Theme.colorScheme.brand.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Theme.colorScheme.brand.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DaysSelectionGridPreview() {
    MongezTheme {
        DaysSelectionGrid(
            selectedDays = setOf("Sun", "Mon"),
            onDayToggle = {}
        )
    }
}

