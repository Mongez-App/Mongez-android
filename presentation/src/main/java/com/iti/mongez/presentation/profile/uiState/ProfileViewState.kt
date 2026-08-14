package com.iti.mongez.presentation.profile.uiState

import com.iti.mongez.domain.settings.model.Language
import com.iti.mongez.domain.preferences.model.UserPreferences

data class ProfileViewState(
    val name: String = "",
    val email: String = "",
    val profilePictureUrl: String? = null,
    val studyingHours: Int = 0,
    val completedTasks: Int = 0,
    val streakDays: Int = 0,
    val isCalendarSyncEnabled: Boolean = false,
    val isCalendarSynced: Boolean = false,
    val lastSyncedAt: String? = null,
    val isDarkModeEnabled: Boolean = false,
    val language: Language = Language.EN,
    val calendarEmail: String? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isEditPreferencesSheetVisible: Boolean = false,
    val selectedStudyHours: Int = UserPreferences.DEFAULT_STUDY_HOURS,
    val selectedDays: Set<String> = UserPreferences.DEFAULT_AVAILABLE_DAYS.toSet(),
    val isEditProfileDialogVisible: Boolean = false,
    val editingName: String = "",
    val selectedAvatarUrl: String? = null,
    val isAvatarPickerVisible: Boolean = false,
    val isLogoutDialogVisible: Boolean = false,
    val isCalendarSyncDialogVisible: Boolean = false,
    val calendarSyncDialogTargetState: Boolean = false
)
