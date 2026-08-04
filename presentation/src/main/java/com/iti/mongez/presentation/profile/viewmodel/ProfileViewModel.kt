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
import com.iti.mongez.domain.preferences.usecase.SavePreferencesLocallyUseCase
import com.iti.mongez.domain.preferences.usecase.SetPreferencesOnboardingCompletedUseCase
import com.iti.mongez.domain.profile.usecase.UpdateProfileUseCase
import com.iti.mongez.domain.calendar.usecase.ConnectCalendarUseCase
import com.iti.mongez.domain.calendar.usecase.DisconnectCalendarUseCase
import com.iti.mongez.domain.calendar.usecase.GetCalendarStatusUseCase
import com.iti.mongez.domain.calendar.usecase.SyncCalendarEventsUseCase
import com.iti.mongez.domain.profile.usecase.UploadProfileImageUseCase
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
    private val savePreferencesUseCase: SavePreferencesUseCase,
    private val savePreferencesLocallyUseCase: SavePreferencesLocallyUseCase,
    private val setPreferencesOnboardingCompletedUseCase: SetPreferencesOnboardingCompletedUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val connectCalendarUseCase: ConnectCalendarUseCase,
    private val disconnectCalendarUseCase: DisconnectCalendarUseCase,
    private val getCalendarStatusUseCase: GetCalendarStatusUseCase,
    private val syncCalendarEventsUseCase: SyncCalendarEventsUseCase,
    private val uploadProfileImageUseCase: UploadProfileImageUseCase
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
            is ProfileIntent.Logout -> {
                _viewState.update { it.copy(isLogoutDialogVisible = true) }
            }
            is ProfileIntent.ConfirmLogout -> logout()
            is ProfileIntent.ToggleLogoutDialog -> {
                _viewState.update { it.copy(isLogoutDialogVisible = intent.visible) }
            }
            is ProfileIntent.ToggleCalendarSyncDialog -> {
                _viewState.update { it.copy(isCalendarSyncDialogVisible = intent.visible) }
            }
            is ProfileIntent.ConfirmCalendarSyncDisconnect -> toggleCalendarSync(false)
            is ProfileIntent.ToggleEditPreferencesSheet -> {
                if (intent.visible) {
                    loadPreferences()
                } else {
                    _viewState.update { it.copy(isEditPreferencesSheetVisible = false) }
                }
            }
            is ProfileIntent.UpdateStudyHours -> {
                _viewState.update { it.copy(selectedStudyHours = intent.hours) }
                savePreferencesLocally()
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
                savePreferencesLocally()
            }
            is ProfileIntent.SavePreferences -> savePreferences()
            is ProfileIntent.ToggleEditProfileDialog -> {
                _viewState.update { 
                    it.copy(
                        isEditProfileDialogVisible = intent.visible,
                        editingName = it.name,
                        selectedAvatarUrl = it.profilePictureUrl
                    )
                }
            }
            is ProfileIntent.UpdateEditingName -> {
                _viewState.update { it.copy(editingName = intent.name) }
            }
            is ProfileIntent.OnAvatarSelected -> {
                _viewState.update { 
                    it.copy(
                        selectedAvatarUrl = intent.avatarUrl,
                        isAvatarPickerVisible = false 
                    ) 
                }
            }
            is ProfileIntent.ToggleAvatarPicker -> {
                _viewState.update { it.copy(isAvatarPickerVisible = intent.visible) }
            }
            ProfileIntent.SubmitProfileUpdate -> submitProfileUpdate()
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

    private fun savePreferencesLocally() {
        viewModelScope.launch {
            val state = _viewState.value
            savePreferencesLocallyUseCase(
                state.selectedStudyHours,
                state.selectedDays.toList()
            )
        }
    }

    private fun savePreferences() {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) }
            val state = _viewState.value
            when (val result = savePreferencesUseCase(state.selectedStudyHours, state.selectedDays.toList())) {
                is Result.Success -> {
                    setPreferencesOnboardingCompletedUseCase(true)
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
            
            // Load Profile and Calendar Status concurrently
            val profileJob = launch {
                when (val result = getUserProfileUseCase()) {
                    is Result.Success -> {
                        val profile = result.data
                        android.util.Log.d("ProfileViewModel", "Load Success. Server Name: ${profile.name}, Server Avatar: ${profile.avatarUrl}")
                        _viewState.update {
                            val finalName = profile.name?.takeIf { n -> 
                                n.isNotBlank() && n != "null" && n != "User" 
                            } ?: profile.email.substringBefore("@")
                            
                            it.copy(
                                name = finalName,
                                email = profile.email,
                                profilePictureUrl = profile.avatarUrl?.takeIf { a -> a.isNotBlank() && a != "null" },
                                studyingHours = profile.totalStudyHours,
                                completedTasks = profile.completedTasksCount,
                                streakDays = profile.currentStreakDays
                            )
                        }
                    }
                    is Result.Failure -> {
                        _viewState.update {
                            it.copy(errorMessage = result.exception.message ?: "Failed to load profile")
                        }
                    }
                    is Result.Loading -> {}
                }
            }

            val calendarJob = launch {
                when (val result = getCalendarStatusUseCase()) {
                    is Result.Success -> {
                        _viewState.update {
                            it.copy(
                                isCalendarSyncEnabled = result.data.isConnected,
                                calendarEmail = result.data.email
                            )
                        }
                    }
                    is Result.Failure -> {
                        // We don't necessarily want to block the whole UI if just calendar status fails
                        android.util.Log.e("ProfileViewModel", "Failed to load calendar status", result.exception)
                    }
                    is Result.Loading -> {}
                }
            }

            profileJob.join()
            calendarJob.join()
            _viewState.update { it.copy(isLoading = false) }
        }
    }

    private fun submitProfileUpdate() {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true) }
            val state = _viewState.value

            val result = updateProfileUseCase(
                name = state.editingName,
                avatarUrl = state.selectedAvatarUrl
            )

            when (result) {
                is Result.Success -> {
                    _viewState.update {
                        it.copy(
                            isLoading = false,
                            isEditProfileDialogVisible = false,
                            profilePictureUrl = result.data.avatarUrl,
                            selectedAvatarUrl = null,
                            name = result.data.name?.takeIf { n -> n.isNotBlank() && n != "null" } ?: it.name
                        )
                    }
                }
                is Result.Failure -> {
                    _viewState.update { it.copy(isLoading = false) }
                    _effect.emit(ProfileEffect.ShowError(result.exception.message ?: "Update failed"))
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun toggleCalendarSync(enabled: Boolean) {
        viewModelScope.launch {
            _viewState.update { it.copy(isLoading = true, isCalendarSyncDialogVisible = false) }
            val result = if (enabled) connectCalendarUseCase() else disconnectCalendarUseCase()
            
            when (result) {
                is Result.Success -> {
                    // Refresh status to get the updated email/state from server
                    when (val statusResult = getCalendarStatusUseCase()) {
                        is Result.Success -> {
                            _viewState.update {
                                it.copy(
                                    isLoading = false,
                                    isCalendarSyncEnabled = statusResult.data.isConnected,
                                    calendarEmail = statusResult.data.email
                                )
                            }
                            // Also update local settings to stay in sync
                            updateSettings { it.copy(isCalendarSyncEnabled = statusResult.data.isConnected) }

                            // If enabled, trigger events sync (Assuming permission is handled in UI)
                            if (enabled) {
                                syncCalendarEvents()
                            }
                        }
                        is Result.Failure -> {
                            _viewState.update { it.copy(isLoading = false) }
                            _effect.emit(ProfileEffect.ShowError("Sync succeeded but failed to fetch status"))
                        }
                        is Result.Loading -> {}
                    }
                }
                is Result.Failure -> {
                    _viewState.update { it.copy(isLoading = false) }
                    _effect.emit(ProfileEffect.ShowError(result.exception.message ?: "Calendar sync update failed"))
                }
                is Result.Loading -> {}
            }
        }
    }

    private fun syncCalendarEvents() {
        viewModelScope.launch {
            syncCalendarEventsUseCase()
        }
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
