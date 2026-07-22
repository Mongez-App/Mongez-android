package com.iti.mongez.presentation.roadmap.uiState

import java.time.LocalDate

data class RoadmapFilterState(
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val selectedCourses: List<String> = emptyList(),
    val selectedEventTypes: List<String> = emptyList()
) {
    val activeFilterCount: Int
        get() = (if (startDate != null && endDate != null) 1 else 0) +
                selectedCourses.size +
                selectedEventTypes.size

    val hasActiveFilters: Boolean
        get() = activeFilterCount > 0
}