package com.iti.mongez.presentation.roadmap.contract

import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType

sealed class RoadmapEvent {
    object LoadRoadmap : RoadmapEvent()
    data class OnBlockClicked(val blockId: String) : RoadmapEvent()
    object OnAddEventClicked : RoadmapEvent()

    data class OnDateRangeChanged(val range: ClosedFloatingPointRange<Float>) : RoadmapEvent()
}

