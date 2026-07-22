package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.roadmap.uiState.RoadmapFilterState
import java.time.format.DateTimeFormatter

@Composable
fun DateRangeSection(
    state: RoadmapFilterState,
    onStateChange: (RoadmapFilterState) -> Unit
) {
    Text(
        text = stringResource(R.string.filter_date_range),
        style = Theme.typography.title.medium,
        fontWeight = FontWeight.SemiBold,
        color = Theme.colorScheme.text.primary,
        modifier = Modifier.padding(bottom = Theme.spacing.md)
    )
    
    CompactCalendarRangePicker(
        startDate = state.startDate,
        endDate = state.endDate,
        onDateRangeSelected = { start, end ->
            onStateChange(state.copy(startDate = start, endDate = end))
        }
    )

    Spacer(modifier = Modifier.height(Theme.spacing.md))
    
    val formatter = DateTimeFormatter.ofPattern("MMM dd")
    val dateText = if (state.startDate != null && state.endDate != null) {
        "${state.startDate.format(formatter)} – ${state.endDate.format(formatter)}"
    } else {
        stringResource(R.string.filter_no_date_selected)
    }

    Text(
        text = stringResource(R.string.filter_selected_range, dateText),
        style = Theme.typography.body.medium,
        color = if (state.startDate != null) Theme.colorScheme.brand.primary else Theme.colorScheme.text.secondary
    )
}