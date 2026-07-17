package com.iti.mongez.presentation.onboarding.contract

sealed class OnboardingIntent {
    object LoadOnboardingPages : OnboardingIntent()
    data class OnPageChanged(val index: Int) : OnboardingIntent()
    object OnNextClicked : OnboardingIntent()
    object OnSkipClicked : OnboardingIntent()
    object OnGetStartedClicked : OnboardingIntent()
}
