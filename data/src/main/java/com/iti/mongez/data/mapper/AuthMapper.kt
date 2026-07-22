package com.iti.mongez.data.mapper

import com.iti.mongez.data.dtos.AuthResponseDto
import com.iti.mongez.data.local.entity.UserEntity
import com.iti.mongez.domain.auth.model.User

// Map Network DTO -> Domain Model
fun AuthResponseDto.toDomain(): User {
    return User(
        id = this.userId ?: "",
        email = this.email ?: "",
        name = this.name ?: "",
        avatarUrl = this.avatarUrl,
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
        totalStudyHours = this.totalStudyHours,
        completedTasksCount = this.completedTasksCount,
        currentStreakDays = this.currentStreakDays
    )
}
