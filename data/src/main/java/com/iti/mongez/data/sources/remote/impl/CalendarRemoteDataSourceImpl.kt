package com.iti.mongez.data.sources.remote.impl

import com.iti.mongez.data.dtos.SyncCalendarEventsRequestDto
import com.iti.mongez.data.dtos.CalendarSyncRequestDto
import com.iti.mongez.data.dtos.CalendarSyncResponseDto
import com.iti.mongez.data.sources.remote.interfaces.CalendarRemoteDataSource
import com.iti.mongez.data.sources.remote.services.ApiService
import javax.inject.Inject

class CalendarRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : CalendarRemoteDataSource {
    override suspend fun syncCalendarEvents(request: SyncCalendarEventsRequestDto) {
        apiService.syncCalendarEvents(request)
    }

    override suspend fun updateCalendarSyncStatus(request: CalendarSyncRequestDto): CalendarSyncResponseDto {
        return apiService.updateCalendarSyncStatus(request)
    }

    override suspend fun getCalendarSyncStatus(): CalendarSyncResponseDto {
        return apiService.getCalendarSyncStatus()
    }
}
