package com.iti.mongez.domain.auth.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val avatarUrl: String?,
    val totalStudyHours: Int,
    val completedTasksCount: Int,
    val currentStreakDays: Int
)
