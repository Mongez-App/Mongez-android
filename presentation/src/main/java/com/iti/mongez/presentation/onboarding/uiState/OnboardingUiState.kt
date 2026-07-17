package com.iti.mongez.presentation.onboarding.uiState

data class OnboardingUiState(
    val isLoading: Boolean = false,
    val currentPageIndex: Int = 0,
    val onboardingPages: List<OnboardingPage> = emptyList(),
    val isLastPage: Boolean = false
)

data class OnboardingPage(
    val title: String,
    val description: String,
    val imageRes: Int? = null
)
