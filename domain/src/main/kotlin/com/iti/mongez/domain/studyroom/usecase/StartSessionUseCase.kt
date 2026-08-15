package com.iti.mongez.domain.studyroom.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.studyroom.model.StartSessionResult
import com.iti.mongez.domain.studyroom.repository.StudyRoomRepository
import javax.inject.Inject

class StartSessionUseCase @Inject constructor(
    private val repository: StudyRoomRepository
) {
    suspend operator fun invoke(courseId: String, estimatedDurationMinutes: Int, linkedTaskId: String): Result<StartSessionResult> {
        return repository.startSession(courseId, estimatedDurationMinutes, linkedTaskId)
    }
}
