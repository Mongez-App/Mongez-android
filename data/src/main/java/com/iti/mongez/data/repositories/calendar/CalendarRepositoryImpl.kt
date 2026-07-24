package com.iti.mongez.data.repositories.calendar

import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.services.ApiService
import com.iti.mongez.domain.calendar.model.CalendarStatus
import com.iti.mongez.domain.calendar.repository.CalendarRepository
import com.iti.mongez.domain.core.Result
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CalendarRepository {

    override suspend fun connect(): Result<Unit> = safeApi {
        apiService.connectCalendar()
    }

    override suspend fun disconnect(): Result<Unit> = safeApi {
        apiService.disconnectCalendar()
    }

    override suspend fun getStatus(): Result<CalendarStatus> = safeApi {
        val dto = apiService.getCalendarStatus()
        CalendarStatus(
            isConnected = dto.isConnected,
            email = dto.email
        )
    }
}
