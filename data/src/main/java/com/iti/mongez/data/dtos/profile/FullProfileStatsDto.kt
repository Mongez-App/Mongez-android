package com.iti.mongez.data.dtos.profile

import com.google.gson.annotations.SerializedName

data class FullProfileStatsDto(
    @SerializedName("total_study_hours") val totalStudyHours: Int?,
    @SerializedName("completed_tasks_count") val completedTasksCount: Int?,
    @SerializedName("current_streak_days") val currentStreakDays: Int?
)