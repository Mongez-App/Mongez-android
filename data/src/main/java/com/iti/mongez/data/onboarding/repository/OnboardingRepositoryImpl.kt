package com.iti.mongez.data.onboarding.repository

import com.iti.mongez.data.onboarding.source.OnboardingLocalDataSource
import com.iti.mongez.domain.onboarding.repository.OnboardingRepository
import javax.inject.Inject
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.core.exceptions.AppException

class OnboardingRepositoryImpl @Inject constructor(
    private val localDataSource: OnboardingLocalDataSource
) : OnboardingRepository {

    override suspend fun setOnboardingCompleted(): Result<Unit> {
        return try {
            localDataSource.setOnboardingCompleted()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(AppException.UnknownException(message = "Failed to save onboarding state", e))
        }
    }

    override suspend fun isOnboardingCompleted(): Result<Boolean> {
        return try {
            val isCompleted = localDataSource.isOnboardingCompleted()
            Result.Success(isCompleted)
        } catch (e: Exception) {
            Result.Failure(AppException.UnknownException(message = "Failed to read onboarding state", e))
        }
    }
}