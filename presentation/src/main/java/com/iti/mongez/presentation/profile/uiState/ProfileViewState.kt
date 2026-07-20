package com.iti.mongez.presentation.profile.uiState

data class ProfileViewState(
    val name: String = "",
    val email: String = "",
    val profilePictureUrl: String? = null,
    val studyingHours: Int = 0,
    val completedTasks: Int = 0,
    val streakDays: Int = 0,
    val isCalendarSyncEnabled: Boolean = false,
    val isDarkModeEnabled: Boolean = false,
    val language: String = "EN",
    val isLoading: Boolean = false
)
