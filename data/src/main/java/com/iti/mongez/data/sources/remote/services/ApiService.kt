package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.FullProfileDto
import com.iti.mongez.data.dtos.AuthResponseDto
import com.iti.mongez.data.dtos.CalendarStatusDto
import com.iti.mongez.data.dtos.WeeklyRoadmapDto
import com.iti.mongez.data.dtos.UserPreferencesDto
import com.iti.mongez.data.dtos.dashboarddtos.DashboardResponseDto
import com.iti.mongez.data.dtos.dashboarddtos.ProfileResponseDto
import retrofit2.http.*

interface ApiService {
    @GET("roadmap/weekly")
    suspend fun getWeeklyRoadmap(@Query("start_date") startDate: String? = null): WeeklyRoadmapDto

    @POST("roadmap/reschedule")
    suspend fun rescheduleBlocks(@Body request: Map<String, Any>): WeeklyRoadmapDto

    @GET("users/me/profile")
    suspend fun getUserProfile(): ProfileResponseDto

    @GET("users/me/profile")
    suspend fun getFullUserProfile(@Query("_t") timestamp: Long = System.currentTimeMillis()): FullProfileDto

    @GET("home/dashboard")
    suspend fun getHomeDashboard(): DashboardResponseDto

    @PUT("users/me/preferences")
    suspend fun updatePreferences(@Body preferences: UserPreferencesDto): Unit

    @POST("auth/handshake")
    suspend fun handshake(@Body request: Any): AuthResponseDto

    @POST("auth/calendar/connect")
    suspend fun connectCalendar(): Unit

    @GET("auth/calendar/status")
    suspend fun getCalendarStatus(): CalendarStatusDto

    @DELETE("auth/calendar/disconnect")
    suspend fun disconnectCalendar(): Unit
}