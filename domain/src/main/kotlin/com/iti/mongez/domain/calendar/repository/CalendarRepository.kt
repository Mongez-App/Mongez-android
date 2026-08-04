package com.iti.mongez.domain.calendar.repository

import com.iti.mongez.domain.calendar.model.CalendarEvent
import com.iti.mongez.domain.calendar.model.CalendarStatus
import com.iti.mongez.domain.core.Result

interface CalendarRepository {
    suspend fun connect(): Result<Unit>
    suspend fun disconnect(): Result<Unit>
    suspend fun getStatus(): Result<CalendarStatus>
    suspend fun getLocalEvents(): Result<List<CalendarEvent>>
    suspend fun syncEvents(events: List<CalendarEvent>): Result<Unit>
}
