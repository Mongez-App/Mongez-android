package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Check
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
    val lineColor = Theme.colorScheme.brand.roadmapTimeline
    val blockColor = when (block.color) {
        StudyBlockColor.PURPLE -> Theme.colorScheme.brand.roadmapPurple
        StudyBlockColor.GREEN -> Theme.colorScheme.brand.roadmapGreen
        StudyBlockColor.ORANGE -> Theme.colorScheme.brand.roadmapOrange
        StudyBlockColor.BLUE -> Theme.colorScheme.brand.roadmapBlue
    }
    
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
            .clickable { onClick() }
            .padding(vertical = Theme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.width(24.dp))
        
        Spacer(modifier = Modifier.width(Theme.spacing.md))

        RoadmapBlockCard(
            courseName = block.courseName.asString(),
            color = blockColor,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(Theme.spacing.md))

        if (block.isCompleted) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Theme.colorScheme.state.success, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Theme.colorScheme.brand.onPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .border(2.dp, lineColor, CircleShape)
            )
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
