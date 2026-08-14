package com.iti.mongez.data.sources.remote.interfaces

import com.iti.mongez.data.dtos.SyncCalendarEventsRequestDto
import com.iti.mongez.data.dtos.CalendarSyncRequestDto
import com.iti.mongez.data.dtos.CalendarSyncResponseDto

interface CalendarRemoteDataSource {
    suspend fun syncCalendarEvents(request: SyncCalendarEventsRequestDto)
    suspend fun updateCalendarSyncStatus(request: CalendarSyncRequestDto): CalendarSyncResponseDto
    suspend fun getCalendarSyncStatus(): CalendarSyncResponseDto
}
