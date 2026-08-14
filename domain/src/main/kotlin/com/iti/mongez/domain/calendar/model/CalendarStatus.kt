package com.iti.mongez.domain.calendar.model

data class CalendarStatus(
    val isConnected: Boolean,
    val isSynced: Boolean = false,
    val lastSyncedAt: String? = null,
    val email: String?
)
