package com.iti.mongez.presentation.profile.contract

import com.iti.mongez.domain.settings.model.Language

sealed interface ProfileIntent {
    data object LoadProfile : ProfileIntent
    data class ToggleCalendarSync(val enabled: Boolean) : ProfileIntent
    data class ToggleDarkMode(val enabled: Boolean) : ProfileIntent
    data class ChangeLanguage(val language: Language) : ProfileIntent
    data object Logout : ProfileIntent
    data object EditPreferences : ProfileIntent
}
