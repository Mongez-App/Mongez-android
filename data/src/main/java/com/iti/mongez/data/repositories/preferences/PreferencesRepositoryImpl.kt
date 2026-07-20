package com.iti.mongez.data.repositories.preferences

import com.iti.mongez.data.sources.local.PreferencesDataSource
import com.iti.mongez.data.sources.remote.services.ApiService
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.preferences.model.UserPreferences
import com.iti.mongez.domain.preferences.repository.PreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataSource: PreferencesDataSource,
    private val apiService: ApiService
) : PreferencesRepository {

    override suspend fun savePreferences(preferences: UserPreferences): Result<UserPreferences> {
        return try {
            // Local save
            dataSource.savePreferences(preferences)

            // Remote save (Ready for when API is implemented)
            /*
            apiService.updatePreferences(
                UserPreferencesDto(
                    dailyStudyHours = preferences.dailyStudyHours,
                    availableDays = preferences.availableDays
                )
            )
            */

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