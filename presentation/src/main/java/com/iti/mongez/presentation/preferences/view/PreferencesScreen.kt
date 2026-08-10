package com.iti.mongez.presentation.preferences.view

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.util.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.components.button.AppSkipButton
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.preferences.components.AvailableDaysStep
import com.iti.mongez.presentation.preferences.components.PreferenceStepHeader
import com.iti.mongez.presentation.preferences.components.StudyHoursStep
import com.iti.mongez.presentation.preferences.components.SyncCalendarStep
import com.iti.mongez.presentation.preferences.contract.PreferencesIntent
import com.iti.mongez.presentation.preferences.uiState.PreferencesEffect
import com.iti.mongez.presentation.preferences.uiState.PreferencesStep
import com.iti.mongez.presentation.preferences.uiState.PreferencesUiState
import com.iti.mongez.presentation.preferences.viewmodel.PreferencesViewModel
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun PreferencesScreen(
    viewModel: PreferencesViewModel,
    onNavigateToDashboard: (Boolean) -> Unit,
    onShowSnackBar: (String) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 3 })

    LaunchedEffect(pagerState.currentPage) {
        viewModel.processIntent(PreferencesIntent.OnStepChanged(pagerState.currentPage))
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is PreferencesEffect.NavigateToDashboard -> onNavigateToDashboard(effect.showDefaultAlert)
                PreferencesEffect.ScrollToNextPage -> {
                    scope.launch {
                        if (pagerState.currentPage < 2) {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    }
                }
                PreferencesEffect.ScrollToPreviousPage -> {
                    scope.launch {
                        if (pagerState.currentPage > 0) {
                            pagerState.animateScrollToPage(pagerState.currentPage - 1)
                        }
                    }
                }
                PreferencesEffect.ScrollToSyncCalendar -> {
                    scope.launch {
                        pagerState.animateScrollToPage(2)
                    }
                }
                is PreferencesEffect.ShowError -> onShowSnackBar(effect.message)
            }
        }
    }

    PreferencesContent(
        state = state,
        pagerState = pagerState,
        onIntent = viewModel::processIntent
    )
}

@Composable
private fun PreferencesContent(
    state: PreferencesUiState,
    pagerState: PagerState,
    onIntent: (PreferencesIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding(),
        topBar = {
            Column {
                AppSkipButton(
                    isVisible = pagerState.currentPage < 2,
                    onSkipClick = { onIntent(PreferencesIntent.OnSkipClicked) }
                )
                PreferenceStepHeader(
                    step = when (pagerState.currentPage) {
                        0 -> 1
                        1 -> 2
                        2 -> 3
                        else -> 1
                    },
                    modifier = Modifier.padding(bottom = Theme.spacing.md)
                )
            }
        },
        containerColor = Theme.colorScheme.surface.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .animateContentSize()
        ) {
            HorizontalPager(
                state = pagerState,
                userScrollEnabled = true,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
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
                                start = 0.9f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )

                            scaleX = lerp(
                                start = 0.9f,
                                stop = 1f,
                                fraction = 1f - pageOffset.coerceIn(0f, 1f)
                            )
                        }
                ) {
                    when (pageIndex) {
                        0 -> StudyHoursStep(state, onIntent)
                        1 -> AvailableDaysStep(state, onIntent)
                        2 -> SyncCalendarStep(onIntent)
                    }
                }
            }

            AnimatedVisibility(
                visible = pagerState.targetPage < 2,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Theme.spacing.lg, vertical = Theme.spacing.xl),
                    horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md)
                ) {
                    if (pagerState.currentPage != 0) {
                        AppButton(
                            text = stringResource(id = R.string.preferences_previous),
                            onClick = { onIntent(PreferencesIntent.OnBackClicked) },
                            variant = AppButtonVariant.Secondary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    AppButton(
                        text = stringResource(id = R.string.preferences_next),
                        onClick = { onIntent(PreferencesIntent.OnNextClicked) },
                        variant = AppButtonVariant.Primary,
                        modifier = Modifier.weight(1f),
                        isLoading = state.isLoading
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreferencesContentPreview() {
    MongezTheme {
        val pagerState = rememberPagerState(pageCount = { 3 })
        PreferencesContent(
            state = PreferencesUiState(
                currentStep = PreferencesStep.StudyHours,
                studyHours = 4,
                selectedDays = setOf("Mon", "Wed", "Fri")
            ),
            pagerState = pagerState,
            onIntent = {}
        )
    }
}

