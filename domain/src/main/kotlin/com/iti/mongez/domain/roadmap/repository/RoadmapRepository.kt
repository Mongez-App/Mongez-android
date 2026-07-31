package com.iti.mongez.domain.roadmap.repository

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.roadmap.model.WeeklyRoadmap

interface RoadmapRepository {
    suspend fun getWeeklyRoadmap(startDate: String? = null): Result<WeeklyRoadmap>
    suspend fun rescheduleBlocks(blockIds: List<String>, reason: String): Result<WeeklyRoadmap>
    suspend fun addEvent(
        courseId: String,
        title: String,
        eventType: String,
        eventDate: String
    ): Result<String>
}
