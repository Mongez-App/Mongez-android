package com.iti.mongez.domain.roadmap.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.roadmap.repository.RoadmapRepository
import javax.inject.Inject

class AddEventUseCase @Inject constructor(
    private val repository: RoadmapRepository
) {
    suspend operator fun invoke(
        courseId: String,
        title: String,
        eventType: String,
        eventDate: String
    ): Result<String> {
        return repository.addEvent(courseId, title, eventType, eventDate)
    }
}
