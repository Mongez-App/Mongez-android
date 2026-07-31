package com.iti.mongez.data.dtos

import com.google.gson.annotations.SerializedName

data class SyncCalendarEventsRequestDto(
    @SerializedName("events") val events: List<CalendarEventDto>
)

data class CalendarEventDto(
    @SerializedName("title") val title: String,
    @SerializedName("start_time") val startTime: Long,
    @SerializedName("end_time") val endTime: Long,
    @SerializedName("location") val location: String?,
    @SerializedName("is_all_day") val isAllDay: Boolean
)
