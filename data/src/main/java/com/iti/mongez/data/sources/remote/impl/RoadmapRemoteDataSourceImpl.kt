package com.iti.mongez.data.sources.remote.impl

import com.iti.mongez.data.dtos.WeeklyRoadmapDto
import com.iti.mongez.data.sources.remote.interfaces.RoadmapRemoteDataSource
import com.iti.mongez.data.sources.remote.services.RoadmapApiService
import javax.inject.Inject

class RoadmapRemoteDataSourceImpl @Inject constructor(
    private val roadmapApiService: RoadmapApiService
) : RoadmapRemoteDataSource {
    override suspend fun getWeeklyRoadmap(startDate: String?): WeeklyRoadmapDto {
        return roadmapApiService.getWeeklyRoadmap(startDate)
    }

    override suspend fun rescheduleBlocks(request: Map<String, Any>): WeeklyRoadmapDto {
        return roadmapApiService.rescheduleBlocks(request)
    }
}
