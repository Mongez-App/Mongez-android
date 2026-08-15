package com.iti.mongez.domain.studyroom.repository

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.studyroom.model.ChatMessageData
import com.iti.mongez.domain.studyroom.model.EndSessionResult
import com.iti.mongez.domain.studyroom.model.StartSessionResult

interface StudyRoomRepository {
    suspend fun startSession(courseId: String, estimatedDurationMinutes: Int, linkedTaskId: String): Result<StartSessionResult>
    suspend fun endSession(sessionId: String, taskCompleted: Boolean, completionTime: String): Result<EndSessionResult>
    
    suspend fun sendChatMessage(taskId: String, message: String): Result<List<ChatMessageData>>
    suspend fun getChatMessages(taskId: String, page: Int, size: Int): Result<List<ChatMessageData>>
    suspend fun deleteChatMessages(taskId: String): Result<Unit>
    
    suspend fun updateTaskSpentTime(taskId: String, taskCompleted: Boolean, activeSpentTimeMinutes: Int): Result<Unit>
}
