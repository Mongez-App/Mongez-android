package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.designsystem.screens.roadmap.RoadmapBlockCard
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.roadmap.uiState.StudyBlockColor
import com.iti.mongez.presentation.roadmap.uiState.StudyBlockUiModel
import com.iti.mongez.presentation.utils.UiText

@Composable
fun TimelineBlockItem(
    block: StudyBlockUiModel,
    isLast: Boolean,
    onClick: () -> Unit
) {
    val lineColor = Theme.colorScheme.border.secondary
    val blockColor = when (block.color) {
        StudyBlockColor.PURPLE -> Theme.colorScheme.brand.primary
        StudyBlockColor.GREEN -> Theme.colorScheme.state.success
        StudyBlockColor.ORANGE -> Theme.colorScheme.state.warning
    }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                if (!isLast) {
                    drawLine(
                        color = lineColor,
                        start = Offset(12.dp.toPx(), 0f),
                        end = Offset(12.dp.toPx(), size.height),
                        strokeWidth = 2.dp.toPx()
                    )
                }
            }
            .padding(bottom = Theme.spacing.lg),
        verticalAlignment = Alignment.Top
    ) {
        // Timeline Line
        Box(
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight()
        )
        
        Spacer(modifier = Modifier.width(Theme.spacing.md))

        RoadmapBlockCard(
            courseName = block.courseName.asString(),
            topic = block.topic.asString(),
            color = blockColor,
            isCompleted = block.isCompleted,
            eventTitle = block.event?.title?.asString(),
            eventIcon = block.event?.let { event ->
                {
                    val icon = when (event.type) {
                        "Quiz" -> Icons.Default.Description
                        "Midterm" -> Icons.AutoMirrored.Filled.Assignment
                        else -> null
                    }
                    if (icon != null) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = blockColor
                        )
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TimelineBlockItemPreview() {
    MongezTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            TimelineBlockItem(
                block = StudyBlockUiModel(
                    id = "1",
                    courseName = UiText.DynamicString("Operating Systems"),
                    topic = UiText.DynamicString("Process Management"),
                    durationMinutes = 60,
                    isCompleted = true,
                    color = StudyBlockColor.PURPLE
                ),
                isLast = false,
                onClick = {}
            )
            TimelineBlockItem(
                block = StudyBlockUiModel(
                    id = "2",
                    courseName = UiText.DynamicString("Algorithms"),
                    topic = UiText.DynamicString("Dynamic Programming"),
                    durationMinutes = 90,
                    isCompleted = false,
                    color = StudyBlockColor.GREEN
                ),
                isLast = true,
                onClick = {}
            )
        }
    }
}
