package com.iti.mongez.presentation.roadmap.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Assignment
import androidx.compose.material.icons.automirrored.rounded.HelpOutline
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Alarm
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.roadmap.uiState.RoadmapFilterState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EventTypeSection(
    availableEventTypes: List<String>,
    state: RoadmapFilterState,
    onStateChange: (RoadmapFilterState) -> Unit,
) {
    Text(
        text = stringResource(R.string.filter_event_types),
        style = Theme.typography.title.medium,
        fontWeight = FontWeight.SemiBold,
        color = Theme.colorScheme.text.primary,
        modifier = Modifier.padding(bottom = Theme.spacing.md)
    )
    
    val iconMap = mapOf(
        "assignment" to Icons.AutoMirrored.Rounded.Assignment,
        "quiz" to Icons.Rounded.Quiz,
        "midterm" to Icons.Rounded.Description,
        "exam" to Icons.Rounded.School,
        "project" to Icons.Rounded.GridView,
        "study" to Icons.AutoMirrored.Rounded.MenuBook,
        "reminder" to Icons.Rounded.Alarm
    )

    val labelMap = mapOf(
        "assignment" to stringResource(R.string.event_assignment_title),
        "quiz" to stringResource(R.string.event_quiz_title),
        "midterm" to stringResource(R.string.event_midterm_title),
        "exam" to stringResource(R.string.event_exam_title),
        "project" to stringResource(R.string.event_project_title),
        "study" to stringResource(R.string.event_study_title)
    )

    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.sm),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.xs),
        modifier = Modifier.fillMaxWidth()
    ) {
        availableEventTypes.forEach { event ->
            val isSelected = state.selectedEventTypes.contains(event)
            FilterChip(
                selected = isSelected,
                onClick = {
                    val updated = if (isSelected) state.selectedEventTypes - event else state.selectedEventTypes + event
                    onStateChange(state.copy(selectedEventTypes = updated))
                },
                leadingIcon = { 
                    iconMap[event]?.let { icon ->
                        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                },
                label = { 
                    Text(
                        text = labelMap[event] ?: event.replaceFirstChar { it.uppercase() }
                    ) 
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Theme.colorScheme.brand.primaryContainer,
                    selectedLabelColor = Theme.colorScheme.brand.primary,
                    selectedLeadingIconColor = Theme.colorScheme.brand.primary
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