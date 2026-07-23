package com.iti.mongez.presentation.courses.contract

import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType



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

    data class ShowSnackbar(
        val message: String,
        val type: AppSnackbarType = AppSnackbarType.Error
    ) : CoursesIntent
}

sealed interface CoursesEffect {
    data class ShowSnackbar(val message: String, val type: AppSnackbarType = AppSnackbarType.Info) : CoursesEffect
    data class NavigateToUploadMaterial(val courseId: String) : CoursesEffect // NEW
}