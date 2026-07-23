package com.iti.mongez.data.dtos

import com.google.gson.annotations.SerializedName
import com.iti.mongez.domain.profile.model.Profile

data class FullProfileDto(
    @SerializedName("user_id") val userId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("stats") val stats: FullProfileStatsDto?
)

data class FullProfileStatsDto(
    @SerializedName("total_study_hours") val totalStudyHours: Int?,
    @SerializedName("completed_tasks_count") val completedTasksCount: Int?,
    @SerializedName("current_streak_days") val currentStreakDays: Int?
)

fun FullProfileDto.toDomain(): Profile {
    return Profile(
        userId = userId.orEmpty(),
        name = name,
        email = email.orEmpty(),
        avatarUrl = avatarUrl,
        totalStudyHours = stats?.totalStudyHours ?: 0,
        completedTasksCount = stats?.completedTasksCount ?: 0,
        currentStreakDays = stats?.currentStreakDays ?: 0
    )
}
