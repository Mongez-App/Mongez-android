package com.iti.mongez.presentation.courses.contract

import android.net.Uri
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType

sealed interface CoursesIntent {
    object LoadCourses : CoursesIntent
    data class SearchQueryChanged(val query: String) : CoursesIntent
    object FilterClicked : CoursesIntent
    object ToggleAddCourseSheet : CoursesIntent
    data class CreateCourse(
        val name: String,
        val courseCode: String,
        val imageUrl: String,
        val startDate: String,
        val examDate: String,
        val materials: List<Uri>,
        val courseType: String,
        val materialUrl: String?
    ) : CoursesIntent

    data class ShowDeleteConfirmation(val courseId: String) : CoursesIntent
    object DismissDeleteConfirmation : CoursesIntent
    data class ConfirmDeleteCourse(val courseId: String) : CoursesIntent

    data class ShowSnackbar(
        val message: String,
        val type: AppSnackbarType = AppSnackbarType.Error
    ) : CoursesIntent
}

sealed interface CoursesEffect {
    data class ShowSnackbar(val message: String, val type: AppSnackbarType = AppSnackbarType.Info) : CoursesEffect
    data class NavigateToUploadMaterial(val courseId: String) : CoursesEffect
    object NavigateBack : CoursesEffect
}