package com.iti.mongez.presentation.preferences.uiState

enum class PreferencesStep {
    StudyHours,
    AvailableDays,
    SyncCalendar
}

data class PreferencesUiState(
    val isLoading: Boolean = false,
    val currentStep: PreferencesStep = PreferencesStep.StudyHours,
    val studyHours: Int = 4,
    val selectedDays: Set<String> = emptySet(),
    val isCalendarSynced: Boolean = false,
    val isSetupComplete: Boolean = false,
    val hoursChosen: Boolean = false,
    val daysChosen: Boolean = false
)
