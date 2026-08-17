package com.iti.mongez.presentation.studyroom.uiState

import com.iti.mongez.presentation.studyroom.view.ChatMessage

data class StudyRoomState(
    val title: String = "",
    val taskId: String = "",
    val courseId: String = "",
    val sessionId: String? = null,
    val timeRemaining: Int = 25 * 60,
    val totalDurationSeconds: Int = 25 * 60,
    val isPaused: Boolean = false,
    val showEndSessionDialog: Boolean = false,
    val inputText: String = "",
    val isLoading: Boolean = false,
    val messages: List<ChatMessage> = emptyList(),
    val isAiTyping: Boolean = false
)
