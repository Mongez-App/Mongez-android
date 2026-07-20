package com.iti.mongez.domain.settings.usecase

import com.iti.mongez.domain.settings.model.AppSettings
import com.iti.mongez.domain.settings.repository.AppSettingsRepository
import javax.inject.Inject

class UpdateAppSettingsUseCase @Inject constructor(
    private val repository: AppSettingsRepository
) {
    suspend operator fun invoke(settings: AppSettings) = repository.updateAppSettings(settings)
}
