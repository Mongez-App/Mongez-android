package com.iti.mongez.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_preferences")
data class PreferencesEntity(
    @PrimaryKey val id: Int = 0,
    val dailyStudyHours: Int,
    val availableDays: String, // Comma-separated list of days
    val isSynced: Boolean = true
)
