package com.iti.mongez.domain.preferences.model

data class UserPreferences(
    val dailyStudyHours: Int,
    val availableDays: List<String>
) {
    companion object {
        const val DEFAULT_STUDY_HOURS = 8
        val DEFAULT_AVAILABLE_DAYS = listOf("Sun", "Mon", "Tue", "Wed", "Thu")
    }
}
