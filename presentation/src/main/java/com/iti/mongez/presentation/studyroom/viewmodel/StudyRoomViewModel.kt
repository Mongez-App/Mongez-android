package com.iti.mongez.presentation.studyroom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.studyroom.usecase.EndSessionUseCase
import com.iti.mongez.domain.studyroom.usecase.StartSessionUseCase
import com.iti.mongez.domain.studyroom.usecase.GetChatMessagesUseCase
import com.iti.mongez.domain.studyroom.usecase.SendChatMessageUseCase
import com.iti.mongez.domain.studyroom.usecase.UpdateTaskSpentTimeUseCase
import com.iti.mongez.presentation.studyroom.contract.StudyRoomEffect
import com.iti.mongez.presentation.studyroom.contract.StudyRoomIntent
import com.iti.mongez.presentation.studyroom.uiState.StudyRoomState
import com.iti.mongez.domain.studyroom.model.ChatRole
import com.iti.mongez.presentation.studyroom.view.ChatMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class StudyRoomViewModel @Inject constructor(
    private val startSessionUseCase: StartSessionUseCase,
    private val endSessionUseCase: EndSessionUseCase,
    private val sendChatMessageUseCase: SendChatMessageUseCase,
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val updateTaskSpentTimeUseCase: UpdateTaskSpentTimeUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(StudyRoomState())
    val state: StateFlow<StudyRoomState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<StudyRoomEffect>()
    val effect: SharedFlow<StudyRoomEffect> = _effect.asSharedFlow()

    private var timerJob: Job? = null
    fun handleIntent(intent: StudyRoomIntent) {
        when (intent) {
            is StudyRoomIntent.Initialize -> {
                if (_state.value.taskId != intent.taskId) {
                    val durationSeconds = intent.durationMinutes * 60
                    _state.update {
                        it.copy(
                            taskId = intent.taskId,
                            title = intent.title,
                            courseId = intent.courseId,
                            timeRemaining = durationSeconds,
                            totalDurationSeconds = durationSeconds,
                            messages = emptyList(),
                            sessionId = null,
                            isLoading = false
                        )
                    }
                    startSession(intent.courseId, intent.durationMinutes, intent.taskId)
                }
            }
            is StudyRoomIntent.ToggleTimer -> {
                _state.update { it.copy(isPaused = !it.isPaused) }
            }
            is StudyRoomIntent.UpdateInputText -> {
                _state.update { it.copy(inputText = intent.text) }
            }
            is StudyRoomIntent.SendMessage -> {
                val currentInput = _state.value.inputText
                val taskId = _state.value.taskId
                if (currentInput.isNotBlank()) {
                    _state.update { 
                        it.copy(
                            messages = it.messages + ChatMessage(currentInput, true),
                            inputText = "",
                            isAiTyping = true
                        )
                    }
                    viewModelScope.launch {
                        when (val result = sendChatMessageUseCase(taskId, currentInput)) {
                            is Result.Success -> {
                                val newMessages = result.data.map { ChatMessage(it.content, it.role == ChatRole.USER) }
                                _state.update { it.copy(messages = it.messages + newMessages, isAiTyping = false) }
                            }
                            is Result.Failure -> {
                                _state.update { it.copy(isAiTyping = false) }
                                _effect.emit(StudyRoomEffect.ShowSnackbar(result.exception.message ?: "Failed to send message"))
                            }
                            else -> {
                                _state.update { it.copy(isAiTyping = false) }
                            }
                        }
                    }
                }
            }
            is StudyRoomIntent.ShowEndSessionDialog -> {
                _state.update { it.copy(showEndSessionDialog = intent.show, isPaused = intent.show) }
            }
            is StudyRoomIntent.EndSession -> {
                _state.update { it.copy(showEndSessionDialog = false, isPaused = true) }
                endSession(intent.isCompleted)
            }
        }
    }

    private fun startSession(courseId: String, durationMinutes: Int, taskId: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = startSessionUseCase(courseId, durationMinutes, taskId)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            sessionId = result.data.sessionId
                        )
                    }
                    startTimer()
                    fetchChatHistory(taskId)
                }
                is Result.Failure -> {
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(StudyRoomEffect.ShowSnackbar(result.exception.message ?: "Failed to start session"))
                    startTimer() // Start timer anyway so the user can study
                    fetchChatHistory(taskId) // Fetch chat anyway
                }
                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun fetchChatHistory(taskId: String) {
        viewModelScope.launch {
            when (val result = getChatMessagesUseCase(taskId)) {
                is Result.Success -> {
                    val messages = result.data.map { ChatMessage(it.content, it.role == ChatRole.USER) }
                    _state.update { it.copy(messages = messages) }
                }
                else -> {}
            }
        }
    }

    private fun endSession(isCompleted: Boolean) {
        val sessionId = _state.value.sessionId
        android.util.Log.d("STUDY_ROOM_DEBUG", "endSession called. isCompleted: $isCompleted, sessionId: $sessionId")
        if (sessionId == null) {
            android.util.Log.e("STUDY_ROOM_DEBUG", "sessionId is null, navigating back.")
            viewModelScope.launch { _effect.emit(StudyRoomEffect.NavigateBack) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val now = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
            android.util.Log.d("STUDY_ROOM_DEBUG", "Calling endSessionUseCase with sessionId: $sessionId, now: $now")
            when (val result = endSessionUseCase(sessionId, isCompleted, now)) {
                is Result.Success -> {
                    android.util.Log.d("STUDY_ROOM_DEBUG", "endSessionUseCase succeeded!")
                    _state.update { it.copy(isLoading = false) }
                    
                    val activeSpentTime = (_state.value.totalDurationSeconds - _state.value.timeRemaining) / 60
                    android.util.Log.d("STUDY_ROOM_DEBUG", "Calling updateTaskSpentTimeUseCase. taskId: ${_state.value.taskId}, taskCompleted: $isCompleted, activeSpentTimeMinutes: $activeSpentTime")
                    
                    // Also update the task itself
                    val updateResult = updateTaskSpentTimeUseCase(
                        taskId = _state.value.taskId,
                        taskCompleted = isCompleted,
                        activeSpentTimeMinutes = activeSpentTime
                    )
                    
                    if (updateResult is Result.Failure) {
                        android.util.Log.e("STUDY_ROOM_DEBUG", "updateTaskSpentTimeUseCase failed!", updateResult.exception)
                        _effect.emit(StudyRoomEffect.ShowSnackbar("Task updated failed: ${updateResult.exception.message}"))
                        delay(3500.milliseconds) // Wait for user to read error
                    } else {
                        android.util.Log.d("STUDY_ROOM_DEBUG", "updateTaskSpentTimeUseCase succeeded!")
                        result.data.alertMessage?.let { msg ->
                            _effect.emit(StudyRoomEffect.ShowSnackbar(msg))
                            delay(2500.milliseconds)
                        }
                        _effect.emit(StudyRoomEffect.NavigateBack)
                    }
                }
                is Result.Failure -> {
                    android.util.Log.e("STUDY_ROOM_DEBUG", "endSessionUseCase failed!", result.exception)
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(StudyRoomEffect.ShowSnackbar(result.exception.message ?: "Failed to end session"))
                    _effect.emit(StudyRoomEffect.NavigateBack) // Navigate back anyway
                }
                else -> {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000L.milliseconds)
                val currentState = _state.value
                if (!currentState.isPaused && currentState.timeRemaining > 0) {
                    val newTime = currentState.timeRemaining - 1
                    if (newTime <= 0) {
                        _state.update { it.copy(timeRemaining = 0, showEndSessionDialog = true, isPaused = true) }
                    } else {
                        _state.update { it.copy(timeRemaining = newTime) }
                    }
                }
            }
        }
    }
}
