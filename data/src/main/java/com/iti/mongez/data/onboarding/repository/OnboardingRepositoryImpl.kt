package com.iti.mongez.data.onboarding.repository

import com.iti.mongez.data.onboarding.source.OnboardingLocalDataSource
import com.iti.mongez.domain.core.exception.UnknownException
import com.iti.mongez.domain.core.result.Result
import com.iti.mongez.domain.onboarding.repository.OnboardingRepository
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val localDataSource: OnboardingLocalDataSource
) : OnboardingRepository {

    override suspend fun setOnboardingCompleted(): Result<Unit> {
        return try {
            localDataSource.setOnboardingCompleted()
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Failure(UnknownException("Failed to save onboarding state", e))
        }
    }

    override suspend fun isOnboardingCompleted(): Result<Boolean> {
        return try {
            val isCompleted = localDataSource.isOnboardingCompleted()
            Result.Success(isCompleted)
        } catch (e: Exception) {
            Result.Failure(UnknownException("Failed to read onboarding state", e))
        }
    }
}