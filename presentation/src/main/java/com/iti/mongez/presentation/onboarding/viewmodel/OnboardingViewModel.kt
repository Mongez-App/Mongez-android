package com.iti.mongez.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.presentation.onboarding.contract.OnboardingIntent
import com.iti.mongez.presentation.onboarding.uiState.OnboardingEffect
import com.iti.mongez.presentation.onboarding.uiState.OnboardingPage
import com.iti.mongez.presentation.onboarding.uiState.OnboardingUiState
import com.iti.mongez.presentation.R
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OnboardingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<OnboardingEffect>()
    val effect = _effect.asSharedFlow()

    init {
        processIntent(OnboardingIntent.LoadOnboardingPages)
    }

    fun processIntent(intent: OnboardingIntent) {
        when (intent) {
            is OnboardingIntent.LoadOnboardingPages -> loadPages()
            is OnboardingIntent.OnPageChanged -> updatePage(intent.index)
            is OnboardingIntent.OnNextClicked -> handleNext()
            is OnboardingIntent.OnSkipClicked -> navigateToHome()
            is OnboardingIntent.OnGetStartedClicked -> navigateToHome()
        }
    }

    private fun loadPages() {
        val pages = listOf(
            OnboardingPage(
                title = "Study without the stress",
                description = "AI creates your perfect study\n" +
                        "schedule automatically.",
                imageRes = R.drawable.onboarding_1
            ),
            OnboardingPage(
                title = "Your time, perfectly\n" +
                        "optimized",
                description = "Sync your calendar and let AI fit\n" +
                        "studying into your free time.",
                imageRes = R.drawable.onboarding_2
            ),
            OnboardingPage(
                title = "Learn with your\n" +
                        "personal AI tutor",
                description = "Summaries, explanations, and\n" +
                        "smart study sessions—all in one\n" +
                        "place.",
                imageRes = R.drawable.onboarding_3
            )
        )
        _uiState.update { it.copy(onboardingPages = pages) }
    }

    private fun updatePage(index: Int) {
        _uiState.update { 
            it.copy(
                currentPageIndex = index,
                isLastPage = index == it.onboardingPages.size - 1
            ) 
        }
    }

    private fun handleNext() {
        val currentState = _uiState.value
        if (currentState.isLastPage) {
            navigateToHome()
        } else {
            // Logic to move to next page will be handled by PagerState in UI, 
            // but we can update state if needed.
        }
    }

    private fun navigateToHome() {
        viewModelScope.launch {
            _effect.emit(OnboardingEffect.NavigateToHome)
        }
    }
}
