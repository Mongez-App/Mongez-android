package com.iti.mongez.presentation.roadmap.uiState

import com.iti.mongez.presentation.utils.UiText

data class RoadmapUiState(
    val isLoading: Boolean = false,
    val roadmapStartDate: String = "",
    val weeks: List<RoadmapWeekUiModel> = emptyList(),

    val isFilterSheetVisible: Boolean = false,

    val activeFilterState: RoadmapFilterState = RoadmapFilterState(),

    val availableCourses: List<String> = listOf("Algorithms", "Database Systems", "Networks", "Operating Systems", "Math", "Physics"),
    val availableEventTypes: List<String> = listOf("Study", "Assignment", "Quiz", "Exam", "Reminder")
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
    val events: List<RoadmapEventUiModel> = emptyList(),
    val tasks: List<RoadmapTaskUiModel> = emptyList(),
    val color: StudyBlockColor = StudyBlockColor.PURPLE
)

enum class StudyBlockColor {
    PURPLE,
    GREEN,
    ORANGE,
    BLUE
}

data class RoadmapEventUiModel(
    val title: UiText,
    val type: String,
    val dateTime: UiText? = null
)

data class RoadmapTaskUiModel(
    val title: UiText,
    val dateTime: UiText? = null
)
