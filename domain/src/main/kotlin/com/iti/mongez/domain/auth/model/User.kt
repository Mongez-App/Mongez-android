package com.iti.mongez.domain.auth.model

data class User(
    val id: String,
    val email: String,
    val name: String,
    val avatarUrl: String?,
    val appearance: String?,
    val language: String?,
    val calendarSyncConnected: Boolean,
    val totalStudyHours: Int,
    val completedTasksCount: Int,
    val currentStreakDays: Int
)
