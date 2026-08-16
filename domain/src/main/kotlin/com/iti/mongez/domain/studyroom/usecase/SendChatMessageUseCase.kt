package com.iti.mongez.domain.studyroom.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.studyroom.model.ChatMessageData
import com.iti.mongez.domain.studyroom.repository.StudyRoomRepository
import javax.inject.Inject

class SendChatMessageUseCase @Inject constructor(
    private val repository: StudyRoomRepository
) {
    suspend operator fun invoke(taskId: String, message: String): Result<List<ChatMessageData>> {
        return repository.sendChatMessage(taskId, message)
    }
}
