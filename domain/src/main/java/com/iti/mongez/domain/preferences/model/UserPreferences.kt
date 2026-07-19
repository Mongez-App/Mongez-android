package com.iti.mongez.domain.preferences.model

data class UserPreferences(
    val dailyStudyHours: Int,
    val availableDays: List<String>
)
