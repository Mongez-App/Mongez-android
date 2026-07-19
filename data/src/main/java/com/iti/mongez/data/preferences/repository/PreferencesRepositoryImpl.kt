package com.iti.mongez.data.preferences.repository

import com.iti.mongez.data.preferences.sources.PreferencesDataSource
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.preferences.model.UserPreferences
import com.iti.mongez.domain.preferences.repository.PreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataSource: PreferencesDataSource
) : PreferencesRepository {

    override suspend fun savePreferences(preferences: UserPreferences): Result<UserPreferences> {
        return try {
            dataSource.savePreferences(preferences)
            Result.Success(preferences)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun getPreferences(): Result<UserPreferences> {
        return try {
            val preferences = dataSource.userPreferencesFlow.first()
            if (preferences != null) {
                Result.Success(preferences)
            } else {
                Result.Failure(Exception("No preferences found"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}
