package com.iti.mongez.navigation

import com.iti.mongez.presentation.core.models.TaskItem

sealed interface AppRoute {
    object Onboarding : AppRoute
    object Login : AppRoute
    object Register : AppRoute
    object Preferences : AppRoute
    data class Dashboard(val showDefaultAlert: Boolean = false) : AppRoute
    object Roadmap : AppRoute
    data class CourseDetails(
        val courseId: String,
        val allowEditing: Boolean = true,
        val showUploadMaterial: Boolean = true
    ) : AppRoute
    object Profile : AppRoute
    data class StudyRoom(val taskId: String, val title: String, val courseId: String, val durationMinutes: Int) : AppRoute
    data class AllTasks(val tasks: List<TaskItem>) : AppRoute
    data class TrackDetails(val teamId: String) : AppRoute
}
