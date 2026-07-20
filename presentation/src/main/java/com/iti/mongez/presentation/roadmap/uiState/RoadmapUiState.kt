package com.iti.mongez.presentation.roadmap.uiState

import com.iti.mongez.presentation.utils.UiText

data class RoadmapUiState(
    val isLoading: Boolean = false,
    val roadmapStartDate: String = "",
    val weeks: List<RoadmapWeekUiModel> = emptyList(),

    val dateRangeSliderValue: ClosedFloatingPointRange<Float> = 0f..100f,
    val filterStartDateDisplay: String = "Mar 12",
    val filterEndDateDisplay: String = "May 30"
)
data class RoadmapWeekUiModel(
    val weekNumber: Int,
    val dateRange: UiText,
    val days: List<RoadmapDayUiModel>
)

data class RoadmapDayUiModel(
    val date: String,
    val dayName: UiText,
    val blocks: List<StudyBlockUiModel>
)

data class StudyBlockUiModel(
    val id: String,
    val courseName: UiText,
    val topic: UiText,
    val durationMinutes: Int,
    val isCompleted: Boolean,
    val event: RoadmapEventUiModel? = null,
    val color: StudyBlockColor = StudyBlockColor.PURPLE
)

enum class StudyBlockColor {
    PURPLE,
    GREEN,
    ORANGE
}

data class RoadmapEventUiModel(
    val title: UiText,
    val type: String
)
