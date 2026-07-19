package com.iti.mongez.data.preferences.repository

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.preferences.model.UserPreferences
import com.iti.mongez.domain.preferences.repository.PreferencesRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor() : PreferencesRepository {
    
    private var cachedPreferences: UserPreferences? = null

    override suspend fun savePreferences(preferences: UserPreferences): Result<UserPreferences> {
        delay(1000) // Simulate network delay
        cachedPreferences = preferences
        return Result.Success(preferences)
    }

    override suspend fun getPreferences(): Result<UserPreferences> {
        delay(500)
        return cachedPreferences?.let { 
            Result.Success(it) 
        } ?: Result.Failure(Exception("No preferences found"))
    }
}
