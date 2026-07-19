package com.iti.mongez.presentation.preferences.uiState

sealed class PreferencesEffect {
    data class NavigateToDashboard(val showDefaultAlert: Boolean) : PreferencesEffect()
    object ScrollToNextPage : PreferencesEffect()
    object ScrollToPreviousPage : PreferencesEffect()
    object ScrollToSyncCalendar : PreferencesEffect()
    data class ShowError(val message: String) : PreferencesEffect()
}
