package com.iti.mongez.presentation.profile.uiState

import com.iti.mongez.domain.settings.model.Language

data class ProfileViewState(
    val name: String = "",
    val email: String = "",
    val profilePictureUrl: String? = null,
    val studyingHours: Int = 0,
    val completedTasks: Int = 0,
    val streakDays: Int = 0,
    val isCalendarSyncEnabled: Boolean = false,
    val isDarkModeEnabled: Boolean = false,
    val language: Language = Language.EN,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEditPreferencesSheetVisible: Boolean = false,
    val selectedStudyHours: Int = 8,
    val selectedDays: Set<String> = emptySet()
)
