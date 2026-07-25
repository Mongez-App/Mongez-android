package com.iti.mongez.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
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
