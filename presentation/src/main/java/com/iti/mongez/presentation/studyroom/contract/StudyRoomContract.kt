package com.iti.mongez.presentation.studyroom.contract

sealed interface StudyRoomIntent {
    data class Initialize(val taskId: String, val title: String, val courseId: String, val durationMinutes: Int) : StudyRoomIntent
    object ToggleTimer : StudyRoomIntent
    data class UpdateInputText(val text: String) : StudyRoomIntent
    object SendMessage : StudyRoomIntent
    data class ShowEndSessionDialog(val show: Boolean) : StudyRoomIntent
    data class EndSession(val isCompleted: Boolean) : StudyRoomIntent
    data class ShowPauseSessionDialog(val show: Boolean) : StudyRoomIntent
    object PauseSession : StudyRoomIntent
}

sealed interface StudyRoomEffect {
    object NavigateBack : StudyRoomEffect
    data class ShowSnackbar(val message: String) : StudyRoomEffect
}
