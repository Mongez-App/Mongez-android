package com.iti.mongez.domain.core.usecase

import com.iti.mongez.domain.auth.repository.AuthRepository
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.onboarding.repository.OnboardingRepository
import com.iti.mongez.domain.utils.StartDestination
import javax.inject.Inject



class GetInitialRouteUseCase @Inject constructor(
    private val onboardingRepository: OnboardingRepository,
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): StartDestination {
        val onboardingResult = onboardingRepository.isOnboardingCompleted()
        
        val isOnboardingCompleted = when (onboardingResult) {
            is Result.Success -> onboardingResult.data
            else -> false
        }

        if (!isOnboardingCompleted) {
            return StartDestination.ONBOARDING
        }

        val hasToken = authRepository.hasToken()
        if (!hasToken) {
            return StartDestination.LOGIN
        }

        return StartDestination.DASHBOARD
    }
}
