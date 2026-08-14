package com.iti.mongez.data.repositories.roadmap

import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.interfaces.CoursesRemoteDataSource
import com.iti.mongez.data.sources.remote.interfaces.RoadmapRemoteDataSource
import com.iti.mongez.data.dtos.toDomain
import com.iti.mongez.data.dtos.coursesdtos.AddEventRequestDto
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.roadmap.model.WeeklyRoadmap
import com.iti.mongez.domain.roadmap.repository.RoadmapRepository
import javax.inject.Inject

class RoadmapRepositoryImpl @Inject constructor(
    private val roadmapRemoteDataSource: RoadmapRemoteDataSource,
    private val coursesRemoteDataSource: CoursesRemoteDataSource
) : RoadmapRepository {

    override suspend fun getWeeklyRoadmap(startDate: String?): Result<WeeklyRoadmap> = safeApi {
        roadmapRemoteDataSource.getWeeklyRoadmap(startDate).toDomain()
    }

    override suspend fun rescheduleBlocks(blockIds: List<String>, reason: String): Result<WeeklyRoadmap> = safeApi {
        val request = mapOf(
            "block_ids" to blockIds,
            "reason" to reason
        )
        roadmapRemoteDataSource.rescheduleBlocks(request).toDomain()
    }

    override suspend fun addEvent(
        courseId: String,
        title: String,
        eventType: String,
        eventDate: String
    ): Result<String> = safeApi {
        val request = AddEventRequestDto(title, eventType, eventDate)
        coursesRemoteDataSource.addCourseEvent(courseId, request).message
    }
}
