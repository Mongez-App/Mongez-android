package com.iti.mongez.presentation.coursedetails.contract

import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType

sealed class CourseDetailsIntent {
    data class SelectTab(val index: Int) : CourseDetailsIntent()
    object ClickBack : CourseDetailsIntent()
    object EditCourse : CourseDetailsIntent()
    object ShowDeleteDialog : CourseDetailsIntent()
    object DismissDeleteDialog : CourseDetailsIntent()
    object DeleteCourse : CourseDetailsIntent()
    data class ClickDocument(val documentId: String) : CourseDetailsIntent()
    data class DeleteDocument(val documentId: String) : CourseDetailsIntent()
    object ClickUploadMaterial : CourseDetailsIntent()
    data class LoadCourse(val courseId: String) : CourseDetailsIntent()

    // Tasks Intents
    data class SelectTaskFilter(val index: Int) : CourseDetailsIntent()
    data class ClickTask(val taskId: String) : CourseDetailsIntent()
}

sealed class CourseDetailsEffect {
    data class ShowSnackbar(val message: String, val type: AppSnackbarType = AppSnackbarType.Info) : CourseDetailsEffect()
    object NavigateBack : CourseDetailsEffect()
}