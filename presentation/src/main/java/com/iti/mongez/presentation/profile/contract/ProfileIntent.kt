package com.iti.mongez.presentation.profile.contract

sealed interface ProfileIntent {
    object LoadProfile : ProfileIntent
    data class ToggleCalendarSync(val enabled: Boolean) : ProfileIntent
    data class ToggleDarkMode(val enabled: Boolean) : ProfileIntent
    data class ChangeLanguage(val language: String) : ProfileIntent
    object Logout : ProfileIntent
}
