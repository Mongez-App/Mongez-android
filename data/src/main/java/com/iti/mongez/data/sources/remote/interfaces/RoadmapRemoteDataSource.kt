package com.iti.mongez.data.sources.remote.interfaces

import com.iti.mongez.data.dtos.WeeklyRoadmapDto

interface RoadmapRemoteDataSource {
    suspend fun getWeeklyRoadmap(startDate: String? = null): WeeklyRoadmapDto
    suspend fun rescheduleBlocks(request: Map<String, Any>): WeeklyRoadmapDto
}
