package com.iti.mongez.presentation.preferences.uiState

sealed class PreferencesEffect {
    object NavigateToDashboard : PreferencesEffect()
    object ScrollToNextPage : PreferencesEffect()
    object ScrollToPreviousPage : PreferencesEffect()
    object ScrollToSyncCalendar : PreferencesEffect()
    data class ShowError(val message: String) : PreferencesEffect()
}
