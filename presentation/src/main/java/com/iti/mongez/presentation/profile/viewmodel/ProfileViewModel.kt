package com.iti.mongez.presentation.profile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.presentation.profile.contract.ProfileEffect
import com.iti.mongez.presentation.profile.contract.ProfileIntent
import com.iti.mongez.presentation.profile.uiState.ProfileViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow(ProfileViewState())
    val viewState: StateFlow<ProfileViewState> = _viewState.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEffect>()
    val effect = _effect.asSharedFlow()

    init {
        processIntent(ProfileIntent.LoadProfile)
    }

    fun processIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.LoadProfile -> loadProfile()
            is ProfileIntent.ToggleCalendarSync -> toggleCalendarSync(intent.enabled)
            is ProfileIntent.ToggleDarkMode -> toggleDarkMode(intent.enabled)
            is ProfileIntent.ChangeLanguage -> changeLanguage(intent.language)
            is ProfileIntent.Logout -> logout()
        }
    }

    private fun loadProfile() {
        _viewState.update {
            it.copy(
                name = "Abdullah Mohamed",
                email = "abdullah@example.com",
                studyingHours = 145,
                completedTasks = 382,
                streakDays = 14,
                isCalendarSyncEnabled = true,
                isDarkModeEnabled = false,
                language = "EN"
            )
        }
    }

    private fun toggleCalendarSync(enabled: Boolean) {
        _viewState.update { it.copy(isCalendarSyncEnabled = enabled) }
    }

    private fun toggleDarkMode(enabled: Boolean) {
        _viewState.update { it.copy(isDarkModeEnabled = enabled) }
    }

    private fun changeLanguage(language: String) {
        _viewState.update { it.copy(language = language) }
    }

    private fun logout() {
        viewModelScope.launch {
            _effect.emit(ProfileEffect.NavigateToLogin)
        }
    }
}
