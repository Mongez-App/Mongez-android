package com.iti.mongez.domain.preferences.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.preferences.model.UserPreferences
import com.iti.mongez.domain.preferences.repository.PreferencesRepository
import javax.inject.Inject

class SavePreferencesLocallyUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(dailyStudyHours: Int, availableDays: List<String>): Result<Unit> {
        return repository.savePreferencesLocally(
            UserPreferences(
                dailyStudyHours = dailyStudyHours,
                availableDays = availableDays
            )
        )
    }
}
