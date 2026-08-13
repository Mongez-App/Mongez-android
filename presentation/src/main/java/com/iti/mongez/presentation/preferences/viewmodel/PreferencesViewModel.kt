package com.iti.mongez.presentation.preferences.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.presentation.preferences.contract.PreferencesIntent
import com.iti.mongez.presentation.preferences.uiState.PreferencesEffect
import com.iti.mongez.presentation.preferences.uiState.PreferencesStep
import com.iti.mongez.presentation.preferences.uiState.PreferencesUiState
import com.iti.mongez.domain.preferences.usecase.SavePreferencesUseCase
import com.iti.mongez.domain.preferences.usecase.SavePreferencesLocallyUseCase
import com.iti.mongez.domain.preferences.usecase.GetPreferencesUseCase
import com.iti.mongez.domain.preferences.usecase.SetPreferencesOnboardingCompletedUseCase
import com.iti.mongez.domain.calendar.usecase.SyncCalendarEventsUseCase
import com.iti.mongez.domain.calendar.usecase.UpdateCalendarSyncStatusUseCase
import com.iti.mongez.domain.settings.usecase.GetAppSettingsUseCase
import com.iti.mongez.domain.settings.usecase.UpdateAppSettingsUseCase
import com.iti.mongez.domain.core.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val savePreferencesUseCase: SavePreferencesUseCase,
    private val savePreferencesLocallyUseCase: SavePreferencesLocallyUseCase,
    private val getPreferencesUseCase: GetPreferencesUseCase,
    private val setPreferencesOnboardingCompletedUseCase: SetPreferencesOnboardingCompletedUseCase,
    private val syncCalendarEventsUseCase: SyncCalendarEventsUseCase,
    private val updateCalendarSyncStatusUseCase: UpdateCalendarSyncStatusUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PreferencesUiState(
            studyHours = DEFAULT_STUDY_HOURS,
            selectedDays = DEFAULT_AVAILABLE_DAYS.toSet()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        loadInitialPreferences()
        loadInitialSettings()
    }

    private fun loadInitialPreferences() {
        viewModelScope.launch {
            when (val result = getPreferencesUseCase()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            studyHours = result.data.dailyStudyHours,
                            selectedDays = result.data.availableDays.toSet(),
                            // If they have preferences on server, consider them chosen if they differ from defaults
                            hoursChosen = result.data.dailyStudyHours != DEFAULT_STUDY_HOURS,
                            daysChosen = result.data.availableDays.isNotEmpty() && result.data.availableDays != DEFAULT_AVAILABLE_DAYS
                        )
                    }
                }
                else -> { /* Use defaults already in state */ }
            }
        }
    }

    private fun loadInitialSettings() {
        viewModelScope.launch {
            val settings = getAppSettingsUseCase().first()
            _uiState.update { it.copy(isCalendarSynced = settings.isCalendarSyncEnabled) }
        }
    }

    private val _effect = MutableSharedFlow<PreferencesEffect>()
    val effect = _effect.asSharedFlow()

    companion object {
        private const val DEFAULT_STUDY_HOURS = 8
        private val DEFAULT_AVAILABLE_DAYS = listOf("Sun", "Mon", "Tue", "Wed", "Thu")
    }

    fun processIntent(intent: PreferencesIntent) {
        when (intent) {
            is PreferencesIntent.OnStudyHoursChanged -> {
                _uiState.update { it.copy(studyHours = intent.hours, hoursChosen = true) }
                saveLocally()
            }
            is PreferencesIntent.OnDaySelected -> {
                _uiState.update { state ->
                    val newDays = if (state.selectedDays.contains(intent.day)) {
                        state.selectedDays - intent.day
                    } else {
                        state.selectedDays + intent.day
                    }
                    state.copy(selectedDays = newDays, daysChosen = true)
                }
                saveLocally()
            }
            is PreferencesIntent.OnStepChanged -> {
                val step = when (intent.index) {
                    0 -> PreferencesStep.StudyHours
                    1 -> PreferencesStep.AvailableDays
                    2 -> PreferencesStep.SyncCalendar
                    else -> PreferencesStep.StudyHours
                }
                _uiState.update { it.copy(currentStep = step) }
            }
            PreferencesIntent.OnNextClicked -> handleNext()
            PreferencesIntent.OnBackClicked -> handleBack()
            PreferencesIntent.OnSkipClicked -> handleSkip()
            PreferencesIntent.OnSyncCalendarClicked -> syncCalendar()
            PreferencesIntent.OnCompleteSetup -> completeSetup()
        }
    }

    private fun handleNext() {
        val nextStep = when (_uiState.value.currentStep) {
            PreferencesStep.StudyHours -> PreferencesStep.AvailableDays
            PreferencesStep.AvailableDays -> PreferencesStep.SyncCalendar
            PreferencesStep.SyncCalendar -> {
                completeSetup()
                return
            }
        }
        _uiState.update { it.copy(currentStep = nextStep) }
        viewModelScope.launch {
            _effect.emit(PreferencesEffect.ScrollToNextPage)
        }
    }

    private fun handleBack() {
        val prevStep = when (_uiState.value.currentStep) {
            PreferencesStep.StudyHours -> return
            PreferencesStep.AvailableDays -> PreferencesStep.StudyHours
            PreferencesStep.SyncCalendar -> PreferencesStep.AvailableDays
        }
        _uiState.update { it.copy(currentStep = prevStep) }
        viewModelScope.launch {
            _effect.emit(PreferencesEffect.ScrollToPreviousPage)
        }
    }

    private fun saveLocally() {
        viewModelScope.launch {
            val state = _uiState.value
            savePreferencesLocallyUseCase(
                state.studyHours,
                state.selectedDays.toList()
            )
        }
    }

    private fun handleSkip() {
        if (_uiState.value.currentStep == PreferencesStep.SyncCalendar) {
            updateCalendarSyncStatus(false)
            updateBackendSyncStatus(connected = false, synced = false)
            saveDefaultPreferencesAndComplete(showDialog = false)
        } else {
            _uiState.update { it.copy(currentStep = PreferencesStep.SyncCalendar) }
            viewModelScope.launch {
                _effect.emit(PreferencesEffect.ScrollToSyncCalendar)
            }
        }
    }

    private fun saveDefaultPreferencesAndComplete(showDialog: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            savePreferencesUseCase(DEFAULT_STUDY_HOURS, DEFAULT_AVAILABLE_DAYS)
            setPreferencesOnboardingCompletedUseCase(true)
            _uiState.update { it.copy(isLoading = false, isSetupComplete = true) }
            _effect.emit(PreferencesEffect.NavigateToDashboard(showDefaultAlert = showDialog))
        }
    }

    private fun updateCalendarSyncStatus(enabled: Boolean) {
        viewModelScope.launch {
            val currentSettings = getAppSettingsUseCase().first()
            updateAppSettingsUseCase(currentSettings.copy(isCalendarSyncEnabled = enabled))
        }
    }

    private fun updateBackendSyncStatus(connected: Boolean, synced: Boolean) {
        viewModelScope.launch {
            updateCalendarSyncStatusUseCase(connected, synced)
        }
    }

    private fun syncCalendar() {
        _uiState.update { it.copy(isCalendarSynced = true) }
        updateCalendarSyncStatus(true)
        updateBackendSyncStatus(connected = true, synced = false)
        viewModelScope.launch {
            val result = syncCalendarEventsUseCase()
            if (result is Result.Success) {
                updateBackendSyncStatus(connected = true, synced = true)
            }
        }
        completeSetup()
    }

    private fun completeSetup() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val currentState = _uiState.value
            savePreferencesUseCase(
                currentState.studyHours,
                currentState.selectedDays.toList()
            )
            setPreferencesOnboardingCompletedUseCase(true)
            
            val showDefaultAlert = !currentState.hoursChosen || !currentState.daysChosen
            
            _uiState.update { it.copy(isLoading = false, isSetupComplete = true) }
            _effect.emit(PreferencesEffect.NavigateToDashboard(showDefaultAlert = showDefaultAlert))
        }
    }
}
