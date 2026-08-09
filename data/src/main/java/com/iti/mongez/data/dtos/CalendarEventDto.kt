package com.iti.mongez.data.dtos

import com.google.gson.annotations.SerializedName

data class SyncCalendarEventsRequestDto(
    @SerializedName("events") val events: List<CalendarEventDto>
)

data class CalendarEventDto(
    @SerializedName("title") val title: String,
    @SerializedName("startDate") val startDate: String,
    @SerializedName("endDate") val endDate: String,
    @SerializedName("eventType") val eventType: String = "CALENDAR"
)
