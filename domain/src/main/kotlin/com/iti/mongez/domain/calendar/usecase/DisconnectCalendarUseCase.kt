package com.iti.mongez.domain.calendar.usecase

import com.iti.mongez.domain.calendar.repository.CalendarRepository
import javax.inject.Inject

class DisconnectCalendarUseCase @Inject constructor(
    private val repository: CalendarRepository
) {
    suspend operator fun invoke() = repository.disconnect()
}
