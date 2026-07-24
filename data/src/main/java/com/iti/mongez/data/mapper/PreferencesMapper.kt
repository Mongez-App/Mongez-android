package com.iti.mongez.data.mapper

import com.iti.mongez.data.dtos.UserPreferencesDto
import com.iti.mongez.domain.preferences.model.UserPreferences

fun UserPreferencesDto.toDomain(): UserPreferences {
    return UserPreferences(
        dailyStudyHours = dailyStudyHours,
        availableDays = availableDays
    )
}

fun UserPreferences.toDto(): UserPreferencesDto {
    return UserPreferencesDto(
        dailyStudyHours = dailyStudyHours,
        availableDays = availableDays
    )
}
