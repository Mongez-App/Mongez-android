package com.iti.mongez.presentation.roadmap.uiState

import com.iti.mongez.presentation.utils.UiText
import java.time.LocalDate

data class RoadmapUiState(
    val isLoading: Boolean = false,
    val roadmapStartDate: String = "",
    val weeks: List<RoadmapWeekUiModel> = emptyList(),

    val isFilterSheetVisible: Boolean = false,
    val isAddEventDialogVisible: Boolean = false,
    val isNoCoursesDialogVisible: Boolean = false,

    val activeFilterState: RoadmapFilterState = RoadmapFilterState(),

    val availableCourses: List<CourseUiModel> = emptyList(),
    val availableEventTypes: List<String> = listOf("assignment", "quiz", "midterm", "exam", "project")
)

data class CourseUiModel(
    val id: String,
    val name: String
)

data class RoadmapWeekUiModel(
    val weekNumber: Int,
    val dateRange: UiText,
    val blocks: List<StudyBlockUiModel>
)

data class StudyBlockUiModel(
    val id: String,
    val courseId: String,
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
    val dateTime: UiText? = null,
    val date: LocalDate? = null
)

data class RoadmapTaskUiModel(
    val title: UiText,
    val dateTime: UiText? = null,
    val date: LocalDate? = null
)
