package com.iti.mongez.domain.settings.repository

import com.iti.mongez.domain.settings.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface AppSettingsRepository {
    fun getAppSettings(): Flow<AppSettings>
    suspend fun updateAppSettings(settings: AppSettings)
}
