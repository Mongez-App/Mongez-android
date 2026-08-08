package com.iti.mongez.data.mapper

import com.iti.mongez.data.dtos.AuthResponseDto
import com.iti.mongez.data.local.entity.UserEntity
import com.iti.mongez.domain.auth.model.User

import com.iti.mongez.domain.dashboard.model.UserProfile

fun AuthResponseDto.toUserProfile(): UserProfile {
    return UserProfile(
        name = this.name ?: "User", // Maps "Mahmoud Tarek"
        avatarUrl = this.avatarUrl,
        currentStreakDays = this.stats?.currentStreakDays ?: 0
    )
}

// Map Network DTO -> Domain Model
fun AuthResponseDto.toDomain(): User {
    return User(
        id = this.userId ?: "",
        email = this.email ?: "",
        name = this.name ?: "",
        avatarUrl = this.avatarUrl,
        appearance = this.appearance,
        language = this.language,
        calendarSyncConnected = this.calendarSyncConnected ?: false,
        totalStudyHours = this.stats?.totalStudyHours ?: 0,
        completedTasksCount = this.stats?.completedTasksCount ?: 0,
        currentStreakDays = this.stats?.currentStreakDays ?: 0
    )
}

// Map Domain Model -> Local DTO (Room)
fun User.toEntity(): UserEntity {
    return UserEntity(
        id = this.id,
        email = this.email,
        name = this.name,
        avatarUrl = this.avatarUrl,
        appearance = this.appearance,
        language = this.language,
        calendarSyncConnected = this.calendarSyncConnected,
        totalStudyHours = this.totalStudyHours,
        completedTasksCount = this.completedTasksCount,
        currentStreakDays = this.currentStreakDays
    )
}

// Map Local DTO (Room) -> Domain Model
fun UserEntity.toDomain(): User {
    return User(
        id = this.id,
        email = this.email,
        name = this.name,
        avatarUrl = this.avatarUrl,
        appearance = this.appearance,
        language = this.language,
        calendarSyncConnected = this.calendarSyncConnected,
        totalStudyHours = this.totalStudyHours,
        completedTasksCount = this.completedTasksCount,
        currentStreakDays = this.currentStreakDays
    )
}
