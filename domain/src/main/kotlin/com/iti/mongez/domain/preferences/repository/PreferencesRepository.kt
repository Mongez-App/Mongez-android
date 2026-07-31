package com.iti.mongez.domain.preferences.repository

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.preferences.model.UserPreferences

interface PreferencesRepository {
    suspend fun savePreferences(preferences: UserPreferences): Result<UserPreferences>
    suspend fun savePreferencesLocally(preferences: UserPreferences): Result<Unit>
    suspend fun getPreferences(): Result<UserPreferences>
    suspend fun isPreferencesSet(): Result<Boolean>
    suspend fun setPreferencesOnboardingCompleted(completed: Boolean): Result<Unit>
}
