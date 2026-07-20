package com.iti.mongez.navigation

sealed interface AppRoute {
    object Onboarding : AppRoute
    object Login : AppRoute
    object Register : AppRoute
    object Preferences : AppRoute
    data class Dashboard(val showDefaultAlert: Boolean = false) : AppRoute
    object Roadmap : AppRoute
    data class CourseDetails(val courseId: String) : AppRoute
}
