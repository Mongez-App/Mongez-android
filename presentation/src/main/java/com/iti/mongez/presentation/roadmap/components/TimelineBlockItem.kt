package com.iti.mongez.presentation.roadmap.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
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
    onClick: () -> Unit
) {
    val lineColor = Theme.colorScheme.brand.roadmapTimeline
    val blockColor = when (block.color) {
        StudyBlockColor.PURPLE -> Theme.colorScheme.brand.roadmapPurple
        StudyBlockColor.GREEN -> Theme.colorScheme.brand.roadmapGreen
        StudyBlockColor.ORANGE -> Theme.colorScheme.brand.roadmapOrange
        StudyBlockColor.BLUE -> Theme.colorScheme.brand.roadmapBlue
    }

    var isExpanded by remember { mutableStateOf(false) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = lineColor,
                    start = Offset(12.dp.toPx(), 0f),
                    end = Offset(12.dp.toPx(), size.height),
                    strokeWidth = 2.dp.toPx()
                )
            }
            .padding(vertical = Theme.spacing.sm),
        verticalAlignment = Alignment.Top
    ) {
        Box(modifier = Modifier.width(Theme.spacing.xl))
        
        Spacer(modifier = Modifier.width(Theme.spacing.md))

        Column(modifier = Modifier.weight(1f)) {
            RoadmapBlockCard(
                courseName = block.courseName.asString(),
                color = blockColor,
                isExpanded = isExpanded,
                isExpandable = block.event != null,
                onClick = {
                    if (block.event != null) {
                        isExpanded = !isExpanded
                    } else {
                        onClick()
                    }
                }
            )

            AnimatedVisibility(visible = isExpanded) {
                block.event?.let { event ->
                    Spacer(modifier = Modifier.height(Theme.spacing.sm))
                    RoadmapEventCard(
                        title = event.title.asString(),
                        type = event.type,
                        dateTime = event.dateTime?.asString() ?: ""
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(Theme.spacing.md))

        Box(
            modifier = Modifier
                .padding(top = Theme.spacing.md)
                .size(Theme.spacing.xl),
            contentAlignment = Alignment.Center
        ) {
            if (block.isCompleted) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Theme.colorScheme.state.success, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Theme.colorScheme.brand.onPrimary,
                        modifier = Modifier.size(Theme.spacing.lg)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .border(Theme.spacing.xxs, lineColor, CircleShape)
                )
            }
        }
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
                    courseName = UiText.DynamicString("Algorithms"),
                    topic = UiText.DynamicString("Dynamic Programming"),
                    durationMinutes = 60,
                    isCompleted = true,
                    color = StudyBlockColor.PURPLE,
                    event = com.iti.mongez.presentation.roadmap.uiState.RoadmapEventUiModel(
                        title = UiText.DynamicString("Algorithms Exam"),
                        type = "Exam",
                        dateTime = UiText.DynamicString("May 8 - 3:00 PM")
                    )
                ),
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
                onClick = {}
            )
        }
    }
}
