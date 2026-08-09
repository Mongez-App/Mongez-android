package com.iti.mongez.data.repositories.preferences

import android.util.Log
import com.iti.mongez.data.local.dao.PreferencesDao
import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.data.mapper.toDto
import com.iti.mongez.data.mapper.toEntity
import com.iti.mongez.data.sources.local.PreferencesDataSource
import com.iti.mongez.data.sources.remote.services.ApiService
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.preferences.model.UserPreferences
import com.iti.mongez.domain.preferences.repository.PreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PreferencesRepositoryImpl @Inject constructor(
    private val dataSource: PreferencesDataSource,
    private val preferencesDao: PreferencesDao,
    private val apiService: ApiService,
) : PreferencesRepository {

    override suspend fun savePreferences(preferences: UserPreferences): Result<UserPreferences> {
        return try {
            // Local save (mark as not synced initially)
            preferencesDao.insertPreferences(preferences.toEntity(isSynced = false))

            // Remote save
            apiService.updatePreferences(preferences.toDto())

            // Mark as synced
            preferencesDao.insertPreferences(preferences.toEntity(isSynced = true))

            Result.Success(preferences)
        } catch (e: Exception) {
            // Still return success if saved locally, even if remote fails
            Log.e("PreferencesRepo", "Failed to update remote preferences, saved locally: ${e.message}")
            Result.Success(preferences)
        }
    }

    override suspend fun savePreferencesLocally(preferences: UserPreferences): Result<Unit> {
        return try {
            preferencesDao.insertPreferences(preferences.toEntity(isSynced = false))
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }

    override suspend fun getPreferences(): Result<UserPreferences> {
        return try {
            // Try remote first
            val remotePrefsDto = apiService.getPreferences()
            val remotePrefs = remotePrefsDto.toDomain()
            
            Log.d("PreferencesRepo", "Remote Preferences fetched successfully: $remotePrefsDto")

            // Update local DB
            preferencesDao.insertPreferences(remotePrefs.toEntity(isSynced = true))
            
            Result.Success(remotePrefs)
        } catch (e: Exception) {
            Log.e("PreferencesRepo", "Network error fetching preferences, falling back to local DB: ${e.message}")
            
            val localEntity = preferencesDao.getPreferences()
            if (localEntity != null) {
                val localPrefs = localEntity.toDomain()
                Log.d("PreferencesRepo", "Returning local preferences: $localPrefs")
                Result.Success(localPrefs)
            } else {
                Result.Failure(e)
            }
        }
    }

    override suspend fun isPreferencesSet(): Result<Boolean> {
        return try {
            // Check local DB first
            val localPrefs = preferencesDao.getPreferences()
            if ((localPrefs != null) && (localPrefs.dailyStudyHours > 0)) {
                return Result.Success(data = true)
            }

            // Check remote if local is empty
            val response = apiService.getPreferences()
            val isSetRemotely = response.dailyStudyHours > 0
            if (isSetRemotely) {
                preferencesDao.insertPreferences(response.toDomain().toEntity(isSynced = true))
                dataSource.setOnboardingCompleted(completed = true)
            }
            Result.Success(data = isSetRemotely)
        } catch (exception: Exception) {
            // Fallback to onboarding flag if both fail
            val isOnboardingCompleted = dataSource.isOnboardingCompletedFlow.first()
            Result.Success(data = isOnboardingCompleted)
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
