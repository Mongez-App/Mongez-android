package com.iti.mongez.domain.preferences.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.preferences.repository.PreferencesRepository
import javax.inject.Inject

class SetPreferencesOnboardingCompletedUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(completed: Boolean): Result<Unit> {
        return repository.setPreferencesOnboardingCompleted(completed)
    }
}
