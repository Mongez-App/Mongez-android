package com.iti.mongez.data.dtos

import com.google.gson.annotations.SerializedName

// Requests
data class HandshakeRequestDto(
    @SerializedName("name") val name: String?,
    @SerializedName("appearance") val appearance: String,
    @SerializedName("language") val language: String
)

// Responses
data class AuthResponseDto(
    @SerializedName("user_id") val userId: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("appearance") val appearance: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("calendar_sync_connected") val calendarSyncConnected: Boolean?,
    @SerializedName("stats") val stats: UserStatsDto?
)

data class UserStatsDto(
    @SerializedName("total_study_hours") val totalStudyHours: Int,
    @SerializedName("completed_tasks_count") val completedTasksCount: Int,
    @SerializedName("current_streak_days") val currentStreakDays: Int
)

// Calendar Sync DTOs
data class CalendarStatusDto(
    @SerializedName("is_connected") val isConnected: Boolean,
    @SerializedName("email") val email: String?
)
