package com.iti.mongez.domain.studyroom.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.studyroom.model.ChatMessageData
import com.iti.mongez.domain.studyroom.repository.StudyRoomRepository
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val repository: StudyRoomRepository
) {
    suspend operator fun invoke(taskId: String, page: Int = 0, size: Int = 30): Result<List<ChatMessageData>> {
        return repository.getChatMessages(taskId, page, size)
    }
}
