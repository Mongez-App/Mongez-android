package com.iti.mongez.domain.settings.usecase

import com.iti.mongez.domain.settings.model.AppSettings
import com.iti.mongez.domain.settings.repository.AppSettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAppSettingsUseCase @Inject constructor(
    private val repository: AppSettingsRepository
) {
    operator fun invoke(): Flow<AppSettings> = repository.getAppSettings()
}
