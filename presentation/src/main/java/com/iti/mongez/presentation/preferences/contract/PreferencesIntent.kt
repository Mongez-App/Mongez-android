package com.iti.mongez.presentation.preferences.contract

sealed class PreferencesIntent {
    data class OnStudyHoursChanged(val hours: Int) : PreferencesIntent()
    data class OnDaySelected(val day: String) : PreferencesIntent()
    data class OnStepChanged(val index: Int) : PreferencesIntent()
    object OnNextClicked : PreferencesIntent()
    object OnBackClicked : PreferencesIntent()
    object OnSkipClicked : PreferencesIntent()
    object OnSyncCalendarClicked : PreferencesIntent()
    object OnCompleteSetup : PreferencesIntent()
}
