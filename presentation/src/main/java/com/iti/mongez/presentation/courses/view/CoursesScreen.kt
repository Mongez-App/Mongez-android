package com.iti.mongez.presentation.courses.view

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.presentation.courses.contract.CoursesEffect
import com.iti.mongez.presentation.courses.viewmodel.CoursesViewModel
import kotlinx.coroutines.delay

/**
 * 1. Stateful Wrapper
 * Handles ViewModel interaction, state collection, and side-effects using Hilt.
 */
@Composable
fun CoursesScreen(
    innerPadding: PaddingValues,
    onCourseClick: (String) -> Unit,
    viewModel: CoursesViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    var topSnackbarMessage by remember { mutableStateOf<String?>(null) }
    var topSnackbarType by remember { mutableStateOf(AppSnackbarType.Info) }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CoursesEffect.ShowSnackbar -> {
                    topSnackbarMessage = effect.message
                    topSnackbarType = effect.type
                }
            }
        }
    }

    LaunchedEffect(topSnackbarMessage) {
        if (topSnackbarMessage != null) {
            delay(3000L)
            topSnackbarMessage = null
        }
    }

    // Pass everything down to the stateless content function
    CoursesScreenContent(
        state = state,
        innerPadding = innerPadding,
        topSnackbarMessage = topSnackbarMessage,
        topSnackbarType = topSnackbarType,
        onIntent = viewModel::handleIntent,
        onCourseClick = onCourseClick
    )
}