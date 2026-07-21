package com.iti.mongez.presentation.coursedetails.contract

sealed class CourseDetailsIntent {
    data class SelectTab(val index: Int) : CourseDetailsIntent()
    object ClickBack : CourseDetailsIntent()
    object EditCourse : CourseDetailsIntent()
    object DeleteCourse : CourseDetailsIntent()
    data class ClickDocument(val documentId: String) : CourseDetailsIntent()
    data class DeleteDocument(val documentId: String) : CourseDetailsIntent()
    object ClickUploadMaterial : CourseDetailsIntent()

    // Tasks Intents
    data class SelectTaskFilter(val index: Int) : CourseDetailsIntent()
    data class ClickTask(val taskId: String) : CourseDetailsIntent()
}