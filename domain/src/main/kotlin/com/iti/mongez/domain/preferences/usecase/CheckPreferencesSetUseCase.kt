package com.iti.mongez.domain.preferences.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.preferences.repository.PreferencesRepository
import javax.inject.Inject

class CheckPreferencesSetUseCase @Inject constructor(
    private val repository: PreferencesRepository
) {
    suspend operator fun invoke(): Boolean {
        return when (val result = repository.isPreferencesSet()) {
            is Result.Success -> result.data
            else -> false
        }
    }
}
