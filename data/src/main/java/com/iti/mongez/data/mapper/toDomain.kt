package com.iti.mongez.data.mapper

import com.iti.mongez.data.dtos.profile.FullProfileDto
import com.iti.mongez.domain.profile.model.Profile

fun FullProfileDto.toDomain(): Profile {
    return Profile(
        userId = userId.orEmpty(),
        name = name,
        email = email.orEmpty(),
        avatarUrl = avatarUrl,
        appearance = appearance,
        language = language,
        totalStudyHours = stats?.totalStudyHours ?: 0,
        completedTasksCount = stats?.completedTasksCount ?: 0,
        currentStreakDays = stats?.currentStreakDays ?: 0
    )
}
