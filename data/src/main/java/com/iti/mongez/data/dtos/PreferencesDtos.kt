package com.iti.mongez.data.dtos

import com.google.gson.annotations.SerializedName

data class UserPreferencesDto(
    @SerializedName("daily_study_hours")
    val dailyStudyHours: Int,
    @SerializedName("available_days")
    val availableDays: List<String>
)
