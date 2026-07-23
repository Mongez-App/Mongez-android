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
    private val getUserProfileUseCase: GetUserProfileUseCase
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
            is ProfileIntent.EditPreferences -> editPreferences()
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

    private fun editPreferences() {
        viewModelScope.launch {
            _effect.emit(ProfileEffect.NavigateToPreferences)
        }
    }
}
