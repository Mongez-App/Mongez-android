package com.iti.mongez.data.repositories.roadmap

import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.services.ApiService
import com.iti.mongez.data.dtos.toDomain
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.roadmap.model.WeeklyRoadmap
import com.iti.mongez.domain.roadmap.repository.RoadmapRepository
import javax.inject.Inject

class RoadmapRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : RoadmapRepository {

    override suspend fun getWeeklyRoadmap(startDate: String?): Result<WeeklyRoadmap> = safeApi {
        apiService.getWeeklyRoadmap(startDate).toDomain()
    }

    override suspend fun rescheduleBlocks(blockIds: List<String>, reason: String): Result<WeeklyRoadmap> = safeApi {
        val request = mapOf(
            "block_ids" to blockIds,
            "reason" to reason
        )
        apiService.rescheduleBlocks(request).toDomain()
    }
}
