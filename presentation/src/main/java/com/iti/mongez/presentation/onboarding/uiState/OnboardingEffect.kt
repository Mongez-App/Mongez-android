package com.iti.mongez.presentation.onboarding.uiState

sealed class OnboardingEffect {
    data class ShowSnackbar(val message: String) : OnboardingEffect()
    object NavigateToHome : OnboardingEffect()
}
