package com.iti.mongez.domain.onboarding.usecase

import com.iti.mongez.domain.core.result.Result
import com.iti.mongez.domain.onboarding.repository.OnboardingRepository
import javax.inject.Inject

/**
 * Used by the App routing logic to determine if the user should see Onboarding or Home.
 */
class CheckOnboardingStatusUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(): Result<Boolean> {
        return repository.isOnboardingCompleted()
    }
}