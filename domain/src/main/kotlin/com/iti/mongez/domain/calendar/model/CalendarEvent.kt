package com.iti.mongez.domain.calendar.model

data class CalendarEvent(
    val title: String,
    val startTimeMillis: Long,
    val endTimeMillis: Long,
    val location: String?,
    val type: String
)
