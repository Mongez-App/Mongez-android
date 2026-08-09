package com.iti.mongez.presentation.profile.contract

import com.iti.mongez.domain.settings.model.Language

sealed interface ProfileIntent {
    data object LoadProfile : ProfileIntent
    data class ToggleCalendarSync(val enabled: Boolean) : ProfileIntent
    data class ToggleDarkMode(val enabled: Boolean) : ProfileIntent
    data class ChangeLanguage(val language: Language) : ProfileIntent
    data object Logout : ProfileIntent
    data class ToggleEditPreferencesSheet(val visible: Boolean) : ProfileIntent
    data class UpdateStudyHours(val hours: Int) : ProfileIntent
    data class ToggleDay(val day: String) : ProfileIntent

    data object SavePreferences : ProfileIntent
    data class ToggleEditProfileDialog(val visible: Boolean) : ProfileIntent
    data class UpdateEditingName(val name: String) : ProfileIntent

    data class OnAvatarSelected(val avatarUrl: String) : ProfileIntent
    data class ToggleAvatarPicker(val visible: Boolean) : ProfileIntent
    data class ToggleLogoutDialog(val visible: Boolean) : ProfileIntent
    data class ToggleCalendarSyncDialog(val visible: Boolean) : ProfileIntent

    data object ConfirmLogout : ProfileIntent
    data object ConfirmCalendarSyncDisconnect : ProfileIntent

    data object SubmitProfileUpdate : ProfileIntent
}
