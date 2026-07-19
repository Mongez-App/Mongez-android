package com.iti.mongez.domain.preferences.repository

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.preferences.model.UserPreferences

interface PreferencesRepository {
    suspend fun savePreferences(preferences: UserPreferences): Result<UserPreferences>
    suspend fun getPreferences(): Result<UserPreferences>
}
