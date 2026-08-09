package com.iti.mongez.domain.calendar.usecase

import com.iti.mongez.domain.calendar.repository.CalendarRepository
import com.iti.mongez.domain.core.Result
import javax.inject.Inject

class SyncCalendarEventsUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val eventsResult = repository.getLocalEvents()
        return if (eventsResult is Result.Success) {
            repository.syncEvents(eventsResult.data)
        } else {
            Result.Failure(Exception("Failed to fetch local events"))
        }
    }
}
