package com.iti.mongez.domain.profile.model

data class Profile(
    val userId: String,
    val name: String?,
    val email: String,
    val avatarUrl: String?,
    val totalStudyHours: Int,
    val completedTasksCount: Int,
    val currentStreakDays: Int
)
