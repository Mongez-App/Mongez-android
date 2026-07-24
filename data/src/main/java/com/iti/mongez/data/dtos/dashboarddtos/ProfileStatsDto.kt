package com.iti.mongez.data.dtos.dashboarddtos

import com.google.gson.annotations.SerializedName

data class ProfileStatsDto(
    @SerializedName("current_streak_days") val currentStreakDays: Int?
)