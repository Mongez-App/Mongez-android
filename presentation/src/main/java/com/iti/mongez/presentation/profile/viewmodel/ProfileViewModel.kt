package com.iti.mongez.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.presentation.profile.contract.ProfileEffect
import com.iti.mongez.presentation.profile.contract.ProfileIntent
import com.iti.mongez.presentation.profile.uiState.ProfileViewState
import com.iti.mongez.domain.settings.model.AppSettings
import com.iti.mongez.domain.settings.model.Language
import com.iti.mongez.domain.settings.usecase.GetAppSettingsUseCase
import com.iti.mongez.domain.settings.usecase.UpdateAppSettingsUseCase
import com.iti.mongez.domain.auth.usecase.LogoutUseCase
import com.iti.mongez.domain.profile.usecase.GetUserProfileUseCase
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.preferences.usecase.GetPreferencesUseCase
import com.iti.mongez.domain.preferences.usecase.SavePreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getAppSettingsUseCase: GetAppSettingsUseCase,
    private val updateAppSettingsUseCase: UpdateAppSettingsUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getPreferencesUseCase: GetPreferencesUseCase,
    private val savePreferencesUseCase: SavePreferencesUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ProfileViewState())
    val viewState: StateFlow<ProfileViewState> = _viewState.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect = _effect.asSharedFlow()

    init {
        observeAppSettings()
        processIntent(ProfileIntent.LoadProfile)
    }

    private fun observeAppSettings() {
        getAppSettingsUseCase()
            .onEach { settings ->
                _viewState.update {
                    it.copy(
                        isCalendarSyncEnabled = settings.isCalendarSyncEnabled,
                        isDarkModeEnabled = settings.isDarkModeEnabled,
                        language = settings.language
                    )
                }
            }.launchIn(viewModelScope)
    }

    fun processIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.LoadProfile -> loadProfile()
            is ProfileIntent.ToggleCalendarSync -> toggleCalendarSync(intent.enabled)
            is ProfileIntent.ToggleDarkMode -> toggleDarkMode(intent.enabled)
            is ProfileIntent.ChangeLanguage -> changeLanguage(intent.language)
            is ProfileIntent.Logout -> logout()
            is ProfileIntent.ToggleEditPreferencesSheet -> {
                if (intent.visible) {
                    loadPreferences()
                } else {
                    _viewState.update { it.copy(isEditPreferencesSheetVisible = false) }
                }
            }
            is ProfileIntent.UpdateStudyHours -> {
                _viewState.update { it.copy(selectedStudyHours = intent.hours) }
            }
            is ProfileIntent.ToggleDay -> {
                _viewState.update { state ->
                    val newDays = if (state.selectedDays.contains(intent.day)) {
                        state.selectedDays - intent.day
                    } else {
                        state.selectedDays + intent.day
                    }
                    state.copy(selectedDays = newDays)
                }
            }
            is ProfileIntent.SavePreferences -> savePreferences()
        }
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) }
            when (val result = getPreferencesUseCase()) {
                is Result.Success -> {
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            isEditPreferencesSheetVisible = true,
                            selectedStudyHours = result.data.dailyStudyHours,
                            selectedDays = result.data.availableDays.toSet()
                        )
                    }
                }
                is Result.Failure -> {
                    _viewState.update { it.copy(isLoading = false) }
                    _effect.emit(ProfileEffect.ShowError(result.exception.message ?: "Failed to load preferences"))
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun savePreferences() {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) }
            val state = _viewState.value
            when (val result = savePreferencesUseCase(state.selectedStudyHours, state.selectedDays.toList())) {
                is Result.Success -> {
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            isEditPreferencesSheetVisible = false,
                            studyingHours = result.data.dailyStudyHours // Update profile stats if applicable
                        )
                    }
                    // Re-load profile to reflect any stat changes if necessary
                    loadProfile()
                }
                is Result.Failure -> {
                    _viewState.update { it.copy(isLoading = false) }
                    _effect.emit(ProfileEffect.ShowError(result.exception.message ?: "Failed to save preferences"))
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true, errorMessage = null) }
            when (val result = getUserProfileUseCase()) {
                is Result.Success -> {
                    val profile = result.data
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            name = profile.name ?: "",
                            email = profile.email,
                            profilePictureUrl = profile.avatarUrl,
                            studyingHours = profile.totalStudyHours,
                            completedTasks = profile.completedTasksCount,
                            streakDays = profile.currentStreakDays
                        )
                    }
                }
                is Result.Failure -> {
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.exception.message ?: "Failed to load profile"
                        )
                    }
                }
                is Result.Loading -> {
                    _viewState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    private fun toggleCalendarSync(enabled: Boolean) {
        updateSettings { it.copy(isCalendarSyncEnabled = enabled) }
    }

    private fun toggleDarkMode(enabled: Boolean) {
        updateSettings { it.copy(isDarkModeEnabled = enabled) }
    }

    private fun changeLanguage(language: Language) {
        updateSettings { it.copy(language = language) }
    }

    private fun updateSettings(update: (AppSettings) -> AppSettings) {
        viewModelScope.launch {
            val currentSettings = AppSettings(
                isCalendarSyncEnabled = _viewState.value.isCalendarSyncEnabled,
                isDarkModeEnabled = _viewState.value.isDarkModeEnabled,
                language = _viewState.value.language
            )
            updateAppSettingsUseCase(update(currentSettings))
        }
    }

    private fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _viewState.update { ProfileViewState() }
            _effect.emit(ProfileEffect.NavigateToLogin)
        }
    }

}
