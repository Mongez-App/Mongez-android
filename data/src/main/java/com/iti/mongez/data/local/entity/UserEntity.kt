package com.iti.mongez.data.local.entity

data class UserEntity(
    val id: String,
    val email: String,
    val name: String,
    val avatarUrl: String?,
    val totalStudyHours: Int,
    val completedTasksCount: Int,
    val currentStreakDays: Int
)
