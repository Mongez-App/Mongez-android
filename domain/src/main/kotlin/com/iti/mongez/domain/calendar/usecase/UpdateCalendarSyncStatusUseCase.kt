package com.iti.mongez.domain.calendar.usecase

import com.iti.mongez.domain.calendar.model.CalendarStatus
import com.iti.mongez.domain.calendar.repository.CalendarRepository
import com.iti.mongez.domain.core.Result
import javax.inject.Inject

class UpdateCalendarSyncStatusUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke(connected: Boolean, synced: Boolean): Result<CalendarStatus> {
        return repository.updateSyncStatus(connected, synced)
    }
}
