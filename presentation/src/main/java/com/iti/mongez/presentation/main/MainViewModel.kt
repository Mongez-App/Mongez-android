package com.iti.mongez.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.preferences.model.UserPreferences
import com.iti.mongez.domain.preferences.usecase.GetPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getPreferencesUseCase: GetPreferencesUseCase
) : ViewModel() {

    private val _preferences = MutableStateFlow<UserPreferences?>(null)
    val preferences: StateFlow<UserPreferences?> = _preferences.asStateFlow()

    init {
        loadPreferences()
    }

    fun loadPreferences() {
        viewModelScope.launch {
            when (val result = getPreferencesUseCase()) {
                is Result.Success -> {
                    _preferences.update { result.data }
                }
                else -> {
                    _preferences.update {
                        UserPreferences(
                            dailyStudyHours = UserPreferences.DEFAULT_STUDY_HOURS,
                            availableDays = UserPreferences.DEFAULT_AVAILABLE_DAYS
                        )
                    }
                }
            }
        }
    }
}
