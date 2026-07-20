package com.iti.mongez.presentation.courses

import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType

data class CoursesState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val allCourses: List<Course> = emptyList(),
    val filteredCourses: List<Course> = emptyList(),
    val isAddCourseSheetVisible: Boolean = false,
    val isCreatingCourse: Boolean = false
)

sealed interface CoursesIntent {
    object LoadCourses : CoursesIntent
    data class SearchQueryChanged(val query: String) : CoursesIntent
    object FilterClicked : CoursesIntent
    object ToggleAddCourseSheet : CoursesIntent
    data class CreateCourse(
        val name: String,
        val courseCode: String,
        val startDate: String,
        val examDate: String,
        val hasMaterials: Boolean
    ) : CoursesIntent
}

sealed interface CoursesEffect {
    data class ShowSnackbar(
        val message: String,
        val type: AppSnackbarType = AppSnackbarType.Info
    ) : CoursesEffect
}