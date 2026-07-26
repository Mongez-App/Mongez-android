package com.iti.mongez.data.repositories.preferences

import com.iti.mongez.data.dtos.UserPreferencesDto
import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.data.mapper.toDto
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

            // Remote save
            apiService.updatePreferences(preferences.toDto())

            Result.Success(preferences)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun savePreferencesLocally(preferences: UserPreferences): Result<Unit> {
        return try {
            dataSource.savePreferences(preferences)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun getPreferences(): Result<UserPreferences> {
        return try {
            val localPrefs = dataSource.userPreferencesFlow.first()
            if (localPrefs != null) {
                Result.Success(localPrefs)
            } else {
                val remotePrefs = apiService.getPreferences().toDomain()
                dataSource.savePreferences(remotePrefs)
                Result.Success(remotePrefs)
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun isPreferencesSet(): Result<Boolean> {
        return try {
            val isOnboardingCompleted = dataSource.isOnboardingCompletedFlow.first()
            if (isOnboardingCompleted) {
                return Result.Success(true)
            }

            val response = apiService.getPreferences()
            val isSetRemotely = response.dailyStudyHours > 0
            if (isSetRemotely) {
                dataSource.savePreferences(response.toDomain())
                dataSource.setOnboardingCompleted(true)
            }
            Result.Success(isSetRemotely)
        } catch (e: Exception) {
            Result.Success(false)
        }
    }

    override suspend fun setPreferencesOnboardingCompleted(completed: Boolean): Result<Unit> {
        return try {
            dataSource.setOnboardingCompleted(completed)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}
