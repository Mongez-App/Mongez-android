package com.iti.mongez.domain.roadmap.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.roadmap.model.WeeklyRoadmap
import com.iti.mongez.domain.roadmap.repository.RoadmapRepository
import javax.inject.Inject

class GetWeeklyRoadmapUseCase @Inject constructor(
    private val repository: RoadmapRepository
) {
    suspend operator fun invoke(startDate: String? = null): Result<WeeklyRoadmap> {
        return repository.getWeeklyRoadmap(startDate)
    }
}
