package com.iti.mongez.domain.studyroom.model

data class StartSessionResult(
    val sessionId: String,
    val courseId: String,
    val linkedTaskId: String,
    val startedAt: String
)

data class EndSessionResult(
    val sessionId: String,
    val courseId: String,
    val linkedTaskId: String,
    val startedAt: String,
    val durationMinutesLogged: Int,
    val taskCompleted: Boolean,
    val completionTime: String,
    val alertMessage: String?
)

data class ChatMessageData(
    val messageId: String,
    val role: ChatRole,
    val content: String,
    val createdAt: String
)

enum class ChatRole {
    USER, ASSISTANT, UNKNOWN;

    companion object {
        fun fromString(value: String?): ChatRole {
            return when (value?.uppercase()) {
                "USER" -> USER
                "ASSISTANT" -> ASSISTANT
                else -> UNKNOWN
            }
        }
    }
}
