package com.iti.mongez.presentation.preferences.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iti.mongez.designsystem.theme.Theme
import kotlinx.coroutines.launch

@Composable
fun StudyHoursPicker(
    selectedHours: Int,
    onHoursChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val hours = (1..12).toList()
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = (selectedHours - 1).coerceIn(0, hours.size - 1))
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val currentCenterIndex by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex
        }
    }

    LaunchedEffect(currentCenterIndex) {
        if (currentCenterIndex in hours.indices) {
            onHoursChanged(hours[currentCenterIndex])
        }
    }

    val shadowColorValue = Theme.colorScheme.brand.primary.copy(alpha = 0.7f)
    val strokeColorValue = Theme.colorScheme.brand.primary.copy(alpha = 0.2f)

    Box(
        modifier = modifier
            .width(200.dp)
            .height(350.dp)
            .drawBehind {
                drawIntoCanvas { canvas ->
                    val shadowPaint = Paint().apply {
                        color = Color.Transparent
                    }
                    val frameworkPaint = shadowPaint.asFrameworkPaint()
                    val blurRadiusPx = 10.dp.toPx()

                    frameworkPaint.setShadowLayer(
                        blurRadiusPx,
                        0f,
                        0f,
                        shadowColorValue.toArgb()
                    )
                    
                    val rectRadius = 100.dp.toPx()
                    canvas.drawRoundRect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = size.height,
                        radiusX = rectRadius,
                        radiusY = rectRadius,
                        paint = shadowPaint
                    )

                    val fillPaint = Paint().apply {
                        style = PaintingStyle.Fill
                        color = Color(0xFFF9F9FF)
                    }
                    canvas.drawRoundRect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = size.height,
                        radiusX = rectRadius,
                        radiusY = rectRadius,
                        paint = fillPaint
                    )

                    val strokePaint = Paint().apply {
                        style = PaintingStyle.Stroke
                        strokeWidth = 1.dp.toPx()
                        color = strokeColorValue
                    }
                    canvas.drawRoundRect(
                        left = 0.5.dp.toPx(),
                        top = 0.5.dp.toPx(),
                        right = size.width - 0.5.dp.toPx(),
                        bottom = size.height - 0.5.dp.toPx(),
                        radiusX = rectRadius,
                        radiusY = rectRadius,
                        paint = strokePaint
                    )
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .background(Theme.colorScheme.brand.primary.copy(alpha = 0.1f))
        )

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            contentPadding = PaddingValues(vertical = 140.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(hours.size) { index ->
                val hour = hours[index]
                val isSelected = index == currentCenterIndex
                
                Box(
                    modifier = Modifier
                        .height(70.dp)
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            scope.launch {
                                listState.animateScrollToItem(index)
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = hour.toString(),
                        style = Theme.typography.headline.large.copy(
                            fontSize = if (isSelected) 36.sp else 24.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Theme.colorScheme.brand.primary 
                                    else Theme.colorScheme.text.tertiary
                        ),
                        modifier = Modifier.alpha(if (isSelected) 1f else 0.5f)
                    )
                }
            }
        }
    }
}
