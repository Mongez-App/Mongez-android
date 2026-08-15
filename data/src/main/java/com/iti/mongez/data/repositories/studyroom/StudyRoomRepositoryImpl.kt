package com.iti.mongez.data.repositories.studyroom

import com.iti.mongez.data.dtos.chatdtos.SendChatMessageRequestDto
import com.iti.mongez.data.dtos.sessiondtos.EndSessionRequestDto
import com.iti.mongez.data.dtos.sessiondtos.StartSessionRequestDto
import com.iti.mongez.data.dtos.tasksdtos.UpdateTaskRequestDto
import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.services.StudySessionApiService
import com.iti.mongez.data.sources.remote.services.TasksApiService
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.studyroom.model.ChatMessageData
import com.iti.mongez.domain.studyroom.model.ChatRole
import com.iti.mongez.domain.studyroom.model.EndSessionResult
import com.iti.mongez.domain.studyroom.model.StartSessionResult
import com.iti.mongez.domain.studyroom.repository.StudyRoomRepository
import javax.inject.Inject

class StudyRoomRepositoryImpl @Inject constructor(
    private val studySessionApiService: StudySessionApiService,
    private val tasksApiService: TasksApiService
) : StudyRoomRepository {

    override suspend fun startSession(courseId: String, estimatedDurationMinutes: Int, linkedTaskId: String): Result<StartSessionResult> = safeApi {
        val request = StartSessionRequestDto(courseId, estimatedDurationMinutes, linkedTaskId)
        val response = studySessionApiService.startSession(request)
        StartSessionResult(
            sessionId = response.sessionId.orEmpty(),
            courseId = response.courseId.orEmpty(),
            linkedTaskId = response.linkedTaskId.orEmpty(),
            startedAt = response.startedAt.orEmpty()
        )
    }

    override suspend fun endSession(sessionId: String, taskCompleted: Boolean, completionTime: String): Result<EndSessionResult> = safeApi {
        val request = EndSessionRequestDto(taskCompleted, completionTime)
        val response = studySessionApiService.endSession(sessionId, request)
        EndSessionResult(
            sessionId = response.sessionId.orEmpty(),
            courseId = response.courseId.orEmpty(),
            linkedTaskId = response.linkedTaskId.orEmpty(),
            startedAt = response.startedAt.orEmpty(),
            durationMinutesLogged = response.durationMinutesLogged ?: 0,
            taskCompleted = response.taskCompleted ?: false,
            completionTime = response.completionTime.orEmpty(),
            alertMessage = response.alert?.message
        )
    }

    override suspend fun sendChatMessage(taskId: String, message: String): Result<List<ChatMessageData>> = safeApi {
        val request = SendChatMessageRequestDto(message)
        val response = tasksApiService.sendChatMessage(taskId, request)
        response.messages?.map { dto ->
            ChatMessageData(
                messageId = dto.messageId.orEmpty(),
                role = ChatRole.fromString(dto.role),
                content = dto.content.orEmpty(),
                createdAt = dto.createdAt.orEmpty()
            )
        } ?: emptyList()
    }

    override suspend fun getChatMessages(taskId: String, page: Int, size: Int): Result<List<ChatMessageData>> = safeApi {
        val response = tasksApiService.getChatMessages(taskId, page, size)
        response.messages?.map { dto ->
            ChatMessageData(
                messageId = dto.messageId.orEmpty(),
                role = ChatRole.fromString(dto.role),
                content = dto.content.orEmpty(),
                createdAt = dto.createdAt.orEmpty()
            )
        } ?: emptyList()
    }

    override suspend fun deleteChatMessages(taskId: String): Result<Unit> = safeApi {
        tasksApiService.deleteChatMessages(taskId)
    }

    override suspend fun updateTaskSpentTime(taskId: String, taskCompleted: Boolean, activeSpentTimeMinutes: Int): Result<Unit> = safeApi {
        val request = UpdateTaskRequestDto(taskCompleted, activeSpentTimeMinutes)
        tasksApiService.updateTask(taskId, request)
    }
}
