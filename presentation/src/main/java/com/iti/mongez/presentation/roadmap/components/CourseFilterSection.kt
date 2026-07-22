package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.roadmap.uiState.RoadmapFilterState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CourseFilterSection(
    availableCourses: List<String>,
    state: RoadmapFilterState,
    onStateChange: (RoadmapFilterState) -> Unit,
) {
    Text(
        text = stringResource(R.string.filter_courses),
        style = Theme.typography.title.medium,
        fontWeight = FontWeight.SemiBold,
        color = Theme.colorScheme.text.primary,
        modifier = Modifier.padding(bottom = Theme.spacing.md)
    )
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.sm),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.xs),
        modifier = Modifier.fillMaxWidth()
    ) {
        availableCourses.forEach { course ->
            val isSelected = state.selectedCourses.contains(course)
            FilterChip(
                selected = isSelected,
                onClick = {
                    val updated = if (isSelected) state.selectedCourses - course else state.selectedCourses + course
                    onStateChange(state.copy(selectedCourses = updated))
                },
                label = { Text(course) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Theme.colorScheme.brand.primaryContainer,
                    selectedLabelColor = Theme.colorScheme.brand.primary
                ),
                border = if (isSelected) null else FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = false,
                    borderColor = Theme.colorScheme.border.primary
                )
            )
        }
    }
}