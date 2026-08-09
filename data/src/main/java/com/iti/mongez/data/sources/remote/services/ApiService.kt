package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.CalendarEventDto
import com.iti.mongez.data.dtos.SyncCalendarEventsRequestDto
import com.iti.mongez.data.dtos.profile.FullProfileDto
import com.iti.mongez.data.dtos.profile.UpdateProfileRequestDto
import com.iti.mongez.data.dtos.profile.UpdateProfileResponseDto
import com.iti.mongez.data.dtos.AuthResponseDto
import com.iti.mongez.data.dtos.HandshakeRequestDto
import com.iti.mongez.data.dtos.CalendarStatusDto
import com.iti.mongez.data.dtos.WeeklyRoadmapDto
import com.iti.mongez.data.dtos.UserPreferencesDto
import com.iti.mongez.data.dtos.dashboarddtos.DashboardResponseDto
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @GET("roadmap/weekly")
    suspend fun getWeeklyRoadmap(@Query("start_date") startDate: String? = null): WeeklyRoadmapDto

    @POST("roadmap/reschedule")
    suspend fun rescheduleBlocks(@Body request: Map<String, Any>): WeeklyRoadmapDto

    @GET("auth/me")
    suspend fun getUserProfile(): AuthResponseDto

    @GET("users/me/profile")
    suspend fun getFullUserProfile(): FullProfileDto

    @Multipart
    @POST("profile/avatar")
    suspend fun uploadProfileImage(
        @Part image: MultipartBody.Part
    ): UpdateProfileResponseDto
    @PATCH("users/me/profile")
    suspend fun updateFullUserProfile(@Body request: UpdateProfileRequestDto): UpdateProfileResponseDto

    @GET("home/dashboard")
    suspend fun getHomeDashboard(): DashboardResponseDto

    @GET("users/me/preferences")
    suspend fun getPreferences(): UserPreferencesDto

    @PUT("users/me/preferences")
    suspend fun updatePreferences(@Body preferences: UserPreferencesDto): Unit

    @POST("auth/handshake")
    suspend fun handshake(@Body request: HandshakeRequestDto): AuthResponseDto

    @POST("calendar/events")
    suspend fun syncCalendarEvents(@Body request: SyncCalendarEventsRequestDto): Unit
}
