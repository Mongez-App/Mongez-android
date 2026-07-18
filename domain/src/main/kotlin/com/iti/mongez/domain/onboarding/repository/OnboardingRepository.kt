package com.iti.mongez.domain.onboarding.repository
import com.iti.mongez.domain.core.Result
/**
 * Interface to be implemented by the Data layer (e.g., DataStore implementation).
 */
interface OnboardingRepository {
    suspend fun setOnboardingCompleted(): Result<Unit>
    suspend fun isOnboardingCompleted(): Result<Boolean>
}