package com.iti.mongez.domain.calendar.repository

import com.iti.mongez.domain.calendar.model.CalendarStatus
import com.iti.mongez.domain.core.Result

interface CalendarRepository {
    suspend fun connect(): Result<Unit>
    suspend fun disconnect(): Result<Unit>
    suspend fun getStatus(): Result<CalendarStatus>
}
