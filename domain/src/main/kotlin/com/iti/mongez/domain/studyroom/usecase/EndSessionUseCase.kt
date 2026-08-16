package com.iti.mongez.domain.studyroom.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.studyroom.model.EndSessionResult
import com.iti.mongez.domain.studyroom.repository.StudyRoomRepository
import javax.inject.Inject

class EndSessionUseCase @Inject constructor(
    private val repository: StudyRoomRepository
) {
    suspend operator fun invoke(sessionId: String, taskCompleted: Boolean, completionTime: String): Result<EndSessionResult> {
        return repository.endSession(sessionId, taskCompleted, completionTime)
    }
}
