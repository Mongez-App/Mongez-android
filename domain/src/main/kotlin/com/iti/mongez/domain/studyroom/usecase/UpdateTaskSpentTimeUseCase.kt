package com.iti.mongez.domain.studyroom.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.studyroom.repository.StudyRoomRepository
import javax.inject.Inject

class UpdateTaskSpentTimeUseCase @Inject constructor(
    private val repository: StudyRoomRepository
) {
    suspend operator fun invoke(taskId: String, taskCompleted: Boolean, activeSpentTimeMinutes: Int): Result<Unit> {
        return repository.updateTaskSpentTime(taskId, taskCompleted, activeSpentTimeMinutes)
    }
}
