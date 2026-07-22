package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.roadmap.contract.RoadmapEvent
import com.iti.mongez.presentation.roadmap.uiState.RoadmapFilterState

@Composable
fun ActiveFiltersRow(
    filterState: RoadmapFilterState,
    onEvent: (RoadmapEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.sm),
            modifier = Modifier.weight(1f)
        ) {
            if (filterState.startDate != null && filterState.endDate != null) {
                item {
                    val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM dd")
                    ActiveChip(
                        text = "${filterState.startDate.format(formatter)} - ${filterState.endDate.format(formatter)}",
                        onClear = { onEvent(RoadmapEvent.RemoveDateFilter) }
                    )
                }
            }
            
            items(filterState.selectedCourses) { course ->
                ActiveChip(
                    text = course,
                    onClear = { onEvent(RoadmapEvent.RemoveCourseFilter(course)) }
                )
            }
            
            items(filterState.selectedEventTypes) { event ->
                ActiveChip(
                    text = event,
                    onClear = { onEvent(RoadmapEvent.RemoveEventTypeFilter(event)) }
                )
            }
        }
        
        TextButton(
            onClick = { onEvent(RoadmapEvent.ClearAllFilters) },
            contentPadding = PaddingValues(horizontal = Theme.spacing.xs)
        ) {
            Text("Clear all", style = Theme.typography.label.small, color = Theme.colorScheme.brand.primary)
        }
    }
}