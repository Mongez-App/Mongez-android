package com.iti.mongez.presentation.roadmap.contract

import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.presentation.roadmap.uiState.RoadmapFilterState

sealed class RoadmapEvent {
    data class LoadRoadmap(val startDate: String? = null) : RoadmapEvent()
    data class OnBlockClicked(val blockId: String) : RoadmapEvent()
    object OnAddEventClicked : RoadmapEvent()
    data class ToggleFilterSheet(val isVisible: Boolean) : RoadmapEvent()
    data class ToggleAddEventDialog(val isVisible: Boolean) : RoadmapEvent()
    data class ApplyFilter(val filterState: RoadmapFilterState) : RoadmapEvent()
    object ClearAllFilters : RoadmapEvent()
    object RemoveDateFilter : RoadmapEvent()
    data class RemoveCourseFilter(val course: String) : RoadmapEvent()
    data class RemoveEventTypeFilter(val eventType: String) : RoadmapEvent()
    data class AddEvent(
        val type: String,
        val course: String,
        val name: String,
        val date: String,
        val time: String
    ) : RoadmapEvent()
}

