package com.iti.mongez.presentation.preferences.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.presentation.preferences.contract.PreferencesIntent
import com.iti.mongez.presentation.preferences.uiState.PreferencesEffect
import com.iti.mongez.presentation.preferences.uiState.PreferencesStep
import com.iti.mongez.presentation.preferences.uiState.PreferencesUiState
import com.iti.mongez.domain.preferences.usecase.SavePreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val savePreferencesUseCase: SavePreferencesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PreferencesUiState(
            studyHours = DEFAULT_STUDY_HOURS,
            selectedDays = DEFAULT_AVAILABLE_DAYS.toSet()
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<PreferencesEffect>()
    val effect = _effect.asSharedFlow()

    companion object {
        private const val DEFAULT_STUDY_HOURS = 8
        private val DEFAULT_AVAILABLE_DAYS = listOf("Sun", "Mon", "Tue", "Wed", "Thu")
    }

    fun processIntent(intent: PreferencesIntent) {
        when (intent) {
            is PreferencesIntent.OnStudyHoursChanged -> {
                _uiState.update { it.copy(studyHours = intent.hours) }
            }
            is PreferencesIntent.OnDaySelected -> {
                _uiState.update { state ->
                    val newDays = if (state.selectedDays.contains(intent.day)) {
                        state.selectedDays - intent.day
                    } else {
                        state.selectedDays + intent.day
                    }
                    state.copy(selectedDays = newDays)
                }
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

    private fun handleSkip() {
        if (_uiState.value.currentStep == PreferencesStep.SyncCalendar) {
            saveDefaultPreferencesAndComplete()
        } else {
            _uiState.update { it.copy(currentStep = PreferencesStep.SyncCalendar) }
            viewModelScope.launch {
                _effect.emit(PreferencesEffect.ScrollToSyncCalendar)
            }
        }
    }

    private fun saveDefaultPreferencesAndComplete() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            savePreferencesUseCase(DEFAULT_STUDY_HOURS, DEFAULT_AVAILABLE_DAYS)
            _uiState.update { it.copy(isLoading = false, isSetupComplete = true) }
            _effect.emit(PreferencesEffect.NavigateToDashboard(showDefaultAlert = true))
        }
    }

    private fun syncCalendar() {
        // Mock sync
        _uiState.update { it.copy(isCalendarSynced = true) }
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
            _uiState.update { it.copy(isLoading = false, isSetupComplete = true) }
            _effect.emit(PreferencesEffect.NavigateToDashboard(showDefaultAlert = false))
        }
    }
}
