package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.roadmap.uiState.RoadmapWeekUiModel
import com.iti.mongez.presentation.utils.UiText

@Composable
fun WeekHeader(week: RoadmapWeekUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Theme.spacing.lg, bottom = Theme.spacing.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Dot and Line start
        Box(
            modifier = Modifier
                .width(24.dp),
            contentAlignment = Alignment.Center
        ) {
             Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Theme.colorScheme.brand.primary)
            )
        }
        
        Spacer(modifier = Modifier.width(Theme.spacing.md))
        
        Column {
            Text(
                text = stringResource(R.string.roadmap_week_number, week.weekNumber),
                style = Theme.typography.title.small,
                fontWeight = FontWeight.SemiBold,
                color = Theme.colorScheme.text.primary
            )
            Text(
                text = week.dateRange.asString(),
                style = Theme.typography.label.small,
                color = Theme.colorScheme.text.tertiary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeekHeaderPreview() {
    MongezTheme {
        WeekHeader(
            week = RoadmapWeekUiModel(
                weekNumber = 1,
                dateRange = UiText.DynamicString("Oct 21 - Oct 27"),
                days = emptyList()
            )
        )
    }
}
