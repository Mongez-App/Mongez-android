package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.DashboardResponseDto
import com.iti.mongez.data.dtos.ProfileResponseDto
import com.iti.mongez.data.dtos.RegisterRequestDto
import com.iti.mongez.data.dtos.LoginRequestDto
import com.iti.mongez.data.dtos.AuthResponseDto
import com.iti.mongez.data.dtos.CalendarStatusDto
import com.iti.mongez.data.dtos.WeeklyRoadmapDto
import com.iti.mongez.data.dtos.UserPreferencesDto
import retrofit2.http.*

interface ApiService {
    @GET("roadmap/weekly")
    suspend fun getWeeklyRoadmap(@Query("start_date") startDate: String? = null): WeeklyRoadmapDto

    @POST("roadmap/reschedule")
    suspend fun rescheduleBlocks(@Body request: Map<String, Any>): WeeklyRoadmapDto

    @GET("users/me/profile")
    suspend fun getUserProfile(): ProfileResponseDto

    @GET("home/dashboard")
    suspend fun getHomeDashboard(): DashboardResponseDto

    @PUT("users/me/preferences")
    suspend fun updatePreferences(@Body preferences: UserPreferencesDto): Unit

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("auth/calendar/connect")
    suspend fun connectCalendar(): Unit

    @GET("auth/calendar/status")
    suspend fun getCalendarStatus(): CalendarStatusDto

    @DELETE("auth/calendar/disconnect")
    suspend fun disconnectCalendar(): Unit
}