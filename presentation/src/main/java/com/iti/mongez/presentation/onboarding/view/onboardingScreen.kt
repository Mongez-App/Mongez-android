package com.iti.mongez.presentation.onboarding.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.util.lerp
import com.iti.mongez.designsystem.components.button.AppSkipButton
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.onboarding.components.OnboardingActions
import com.iti.mongez.presentation.onboarding.components.OnboardingIndicator
import com.iti.mongez.presentation.onboarding.components.OnboardingPageItem
import com.iti.mongez.presentation.onboarding.contract.OnboardingIntent
import com.iti.mongez.presentation.onboarding.uiState.OnboardingEffect
import com.iti.mongez.presentation.onboarding.uiState.OnboardingUiState
import com.iti.mongez.presentation.onboarding.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onNavigateToHome: () -> Unit,
    onShowSnackBar: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { state.onboardingPages.size })
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is OnboardingEffect.NavigateToHome -> onNavigateToHome()
                is OnboardingEffect.ShowSnackbar -> onShowSnackBar(effect.message)
                is OnboardingEffect.ScrollToNextPage -> {
                    scope.launch {
                        if (pagerState.currentPage < pagerState.pageCount - 1) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }
            }
        }
    }

    OnboardingContent(
        state = state,
        pagerState = pagerState,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun OnboardingContent(
    state: OnboardingUiState,
    pagerState: PagerState,
    onIntent: (OnboardingIntent) -> Unit
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        onIntent(OnboardingIntent.OnPageChanged(pagerState.currentPage))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.surface.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        AppSkipButton(
            isVisible = !state.isLastPage,
            onSkipClick = { onIntent(OnboardingIntent.OnSkipClicked) }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            if (pageIndex < state.onboardingPages.size) {
                val page = state.onboardingPages[pageIndex]
                OnboardingPageItem(
                    page = page,
                    modifier = Modifier.graphicsLayer {
                        val pageOffset = (
                                (pagerState.currentPage - pageIndex) + pagerState
                                    .currentPageOffsetFraction
                                ).absoluteValue

                        alpha = lerp(
                            start = 0.5f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        scaleY = lerp(
                            start = 0.8f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )

                        scaleX = lerp(
                            start = 0.8f,
                            stop = 1f,
                            fraction = 1f - pageOffset.coerceIn(0f, 1f)
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(Theme.spacing.lg))

        OnboardingIndicator(
            pagerState = pagerState
        )

        Spacer(modifier = Modifier.height(Theme.spacing.xxl))

        OnboardingActions(
            isFirstPage = pagerState.currentPage == 0,
            isLastPage = state.isLastPage,
            onNextClick = { onIntent(OnboardingIntent.OnNextClicked) },
            onBackClick = {
                scope.launch {
                    if (pagerState.currentPage > 0) {
                        pagerState.animateScrollToPage(pagerState.currentPage - 1)
                    }
                }
            },
            onGetStartedClick = { onIntent(OnboardingIntent.OnGetStartedClicked) }
        )
    }
}
