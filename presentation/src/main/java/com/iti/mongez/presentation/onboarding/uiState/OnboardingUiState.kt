package com.iti.mongez.presentation.onboarding.uiState

import androidx.annotation.StringRes
import androidx.annotation.DrawableRes

data class OnboardingUiState(
    val isLoading: Boolean = false,
    val currentPageIndex: Int = 0,
    val onboardingPages: List<OnboardingPage> = emptyList(),
    val isLastPage: Boolean = false
)

data class OnboardingPage(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int? = null
)
