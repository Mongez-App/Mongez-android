package com.iti.mongez.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.presentation.onboarding.contract.OnboardingIntent
import com.iti.mongez.presentation.onboarding.uiState.OnboardingEffect
import com.iti.mongez.presentation.onboarding.uiState.OnboardingPage
import com.iti.mongez.presentation.onboarding.uiState.OnboardingUiState
import com.iti.mongez.presentation.R
import com.iti.mongez.domain.onboarding.usecase.CompleteOnboardingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val completeOnboardingUseCase: CompleteOnboardingUseCase
) : ViewModel() {

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
                titleRes = R.string.onboarding_page1_title,
                descriptionRes = R.string.onboarding_page1_description,
                imageRes = R.drawable.onboarding_1
            ),
            OnboardingPage(
                titleRes = R.string.onboarding_page2_title,
                descriptionRes = R.string.onboarding_page2_description,
                imageRes = R.drawable.onboarding_2
            ),
            OnboardingPage(
                titleRes = R.string.onboarding_page3_title,
                descriptionRes = R.string.onboarding_page3_description,
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
            viewModelScope.launch {
                _effect.emit(OnboardingEffect.ScrollToNextPage)
            }
        }
    }

    private fun navigateToHome() {
        viewModelScope.launch {
            // Save onboarding completion state in DataStore via UseCase
            completeOnboardingUseCase()
            _effect.emit(OnboardingEffect.NavigateToHome)
        }
    }
}
