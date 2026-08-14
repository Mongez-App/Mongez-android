package com.iti.mongez.data.sources.remote.services


import com.iti.mongez.data.dtos.SyncCalendarEventsRequestDto
import com.iti.mongez.data.dtos.CalendarSyncRequestDto
import com.iti.mongez.data.dtos.CalendarSyncResponseDto
import com.iti.mongez.data.dtos.dashboarddtos.DashboardResponseDto
import retrofit2.http.*

interface ApiService {
    @GET("home/dashboard")
    suspend fun getHomeDashboard(): DashboardResponseDto

    @POST("calendar/events")
    suspend fun syncCalendarEvents(@Body request: SyncCalendarEventsRequestDto)

    @PATCH("auth/calendar-sync")
    suspend fun updateCalendarSyncStatus(@Body request: CalendarSyncRequestDto): CalendarSyncResponseDto

    @GET("auth/calendar-sync")
    suspend fun getCalendarSyncStatus(): CalendarSyncResponseDto
}
