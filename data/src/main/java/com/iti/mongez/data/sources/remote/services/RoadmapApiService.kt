package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.WeeklyRoadmapDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface RoadmapApiService {
    @GET("roadmap/weekly")
    suspend fun getWeeklyRoadmap(@Query("start_date") startDate: String? = null): WeeklyRoadmapDto

    @POST("roadmap/reschedule")
    suspend fun rescheduleBlocks(@Body request: Map<String, @JvmSuppressWildcards Any>): WeeklyRoadmapDto
}
