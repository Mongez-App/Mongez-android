package com.iti.mongez.presentation.studyroom.contract

sealed interface StudyRoomIntent {
    data class Initialize(val taskId: String, val title: String) : StudyRoomIntent
    object ToggleTimer : StudyRoomIntent
    data class UpdateInputText(val text: String) : StudyRoomIntent
    object SendMessage : StudyRoomIntent
    data class ShowEndSessionDialog(val show: Boolean) : StudyRoomIntent
    object EndSession : StudyRoomIntent
}

sealed interface StudyRoomEffect {
    object NavigateBack : StudyRoomEffect
}
