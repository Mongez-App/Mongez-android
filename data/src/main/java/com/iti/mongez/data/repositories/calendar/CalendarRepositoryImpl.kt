package com.iti.mongez.data.repositories.calendar

import android.util.Log
import com.iti.mongez.data.dtos.CalendarEventDto
import com.iti.mongez.data.dtos.SyncCalendarEventsRequestDto
import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.local.CalendarLocalDataSource
import com.iti.mongez.data.sources.remote.services.ApiService
import com.iti.mongez.domain.calendar.model.CalendarEvent
import com.iti.mongez.domain.calendar.model.CalendarStatus
import com.iti.mongez.domain.calendar.repository.CalendarRepository
import com.iti.mongez.domain.core.Result
import java.time.Instant
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val localDataSource: CalendarLocalDataSource
) : CalendarRepository {

    override suspend fun connect(): Result<Unit> = Result.Success(Unit)

    override suspend fun disconnect(): Result<Unit> = Result.Success(Unit)

    override suspend fun getStatus(): Result<CalendarStatus> = Result.Success(
        CalendarStatus(isConnected = true, email = null)
    )

    override suspend fun getLocalEvents(): Result<List<CalendarEvent>> {
        return try {
            Result.Success(localDataSource.fetchLocalEvents())
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun syncEvents(events: List<CalendarEvent>): Result<Unit> = safeApi {
        Log.d("CalendarSync", "Syncing ${events.size} events to backend...")
        
        val request = SyncCalendarEventsRequestDto(
            events = events.map {
                CalendarEventDto(
                    title = it.title,
                    startDate = Instant.ofEpochMilli(it.startTimeMillis).toString(),
                    endDate = Instant.ofEpochMilli(it.endTimeMillis).toString(),
                    eventType = "CALENDAR"
                )
            }
        )
        
        if (request.events.isNotEmpty()) {
            val first = request.events.first()
            Log.d("CalendarSync", "Payload preview: '${first.title}' from ${first.startDate} to ${first.endDate}")
        }
        
        apiService.syncCalendarEvents(request)
        Log.d("CalendarSync", "Sync completed successfully!")
    }
}
