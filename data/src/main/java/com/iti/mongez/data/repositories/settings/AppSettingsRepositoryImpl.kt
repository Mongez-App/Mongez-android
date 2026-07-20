package com.iti.mongez.data.repositories.settings

import com.iti.mongez.data.sources.local.PreferencesDataSource
import com.iti.mongez.domain.settings.model.AppSettings
import com.iti.mongez.domain.settings.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AppSettingsRepositoryImpl @Inject constructor(
    private val dataSource: PreferencesDataSource
) : AppSettingsRepository {
    override fun getAppSettings(): Flow<AppSettings> = dataSource.appSettingsFlow

    override suspend fun updateAppSettings(settings: AppSettings) {
        dataSource.updateAppSettings(settings)
    }
}
