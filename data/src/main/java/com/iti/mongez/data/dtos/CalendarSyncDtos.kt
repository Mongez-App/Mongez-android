package com.iti.mongez.data.dtos

import com.google.gson.annotations.SerializedName

data class CalendarSyncRequestDto(
    @SerializedName("calendar_connected") val calendarConnected: Boolean,
    @SerializedName("calendar_synced") val calendarSynced: Boolean
)

data class CalendarSyncResponseDto(
    @SerializedName("calendar_connected") val calendarConnected: Boolean,
    @SerializedName("calendar_synced") val calendarSynced: Boolean,
    @SerializedName("last_calendar_sync_at") val lastCalendarSyncAt: String?
)
