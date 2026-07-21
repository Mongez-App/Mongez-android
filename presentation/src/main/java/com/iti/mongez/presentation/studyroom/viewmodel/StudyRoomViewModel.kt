package com.iti.mongez.presentation.studyroom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.presentation.studyroom.contract.StudyRoomEffect
import com.iti.mongez.presentation.studyroom.contract.StudyRoomIntent
import com.iti.mongez.presentation.studyroom.uiState.StudyRoomState
import com.iti.mongez.presentation.studyroom.view.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class StudyRoomViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(StudyRoomState())
    val state: StateFlow<StudyRoomState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<StudyRoomEffect>()
    val effect: SharedFlow<StudyRoomEffect> = _effect.asSharedFlow()

    init {
        startTimer()
    }

    fun handleIntent(intent: StudyRoomIntent) {
        when (intent) {
            is StudyRoomIntent.Initialize -> {
                _state.update { it.copy(taskId = intent.taskId, title = intent.title) }
            }
            is StudyRoomIntent.ToggleTimer -> {
                _state.update { it.copy(isPaused = !it.isPaused) }
            }
            is StudyRoomIntent.UpdateInputText -> {
                _state.update { it.copy(inputText = intent.text) }
            }
            is StudyRoomIntent.SendMessage -> {
                val currentInput = _state.value.inputText
                if (currentInput.isNotBlank()) {
                    _state.update { 
                        it.copy(
                            messages = it.messages + ChatMessage(currentInput, true),
                            inputText = ""
                        )
                    }
                }
            }
            is StudyRoomIntent.ShowEndSessionDialog -> {
                _state.update { it.copy(showEndSessionDialog = intent.show) }
            }
            is StudyRoomIntent.EndSession -> {
                _state.update { it.copy(showEndSessionDialog = false) }
                viewModelScope.launch {
                    _effect.emit(StudyRoomEffect.NavigateBack)
                }
            }
        }
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                delay(1000L.milliseconds)
                val currentState = _state.value
                if (!currentState.isPaused && currentState.timeRemaining > 0) {
                    val newTime = currentState.timeRemaining - 1
                    if (newTime == 0) {
                        _state.update { it.copy(timeRemaining = 0, showEndSessionDialog = true) }
                    } else {
                        _state.update { it.copy(timeRemaining = newTime) }
                    }
                }
            }
        }
    }
}
