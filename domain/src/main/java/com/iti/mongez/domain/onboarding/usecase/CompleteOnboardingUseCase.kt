package com.iti.mongez.domain.onboarding.usecase

import com.iti.mongez.domain.core.result.Result
import com.iti.mongez.domain.onboarding.repository.OnboardingRepository
import javax.inject.Inject

/**
 * Single-responsibility agent called when OnGetStartedClicked or OnSkipClicked is dispatched.
 */
class CompleteOnboardingUseCase @Inject constructor(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.setOnboardingCompleted()
    }
}