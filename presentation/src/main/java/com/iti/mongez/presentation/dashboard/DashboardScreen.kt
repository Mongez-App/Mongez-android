package com.iti.mongez.presentation.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.designsystem.components.avatar.AppAvatar
import com.iti.mongez.designsystem.components.section.AppSectionHeader
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.screens.dashboard.*
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun DashboardScreen(
    innerPadding: PaddingValues,
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToFocus: () -> Unit,
    onViewAllTasks: () -> Unit,
    onViewAllDeadlines: () -> Unit,
    onNavigateToStudyRoom: (String, String) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    var topSnackbarMessageRes by remember { mutableStateOf<Int?>(null) }
    var topSnackbarType by remember { mutableStateOf<AppSnackbarType?>(null) }

    LaunchedEffect(key1 = true) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is DashboardEffect.ShowSnackbar -> {
                    topSnackbarMessageRes = effect.messageRes
                    topSnackbarType = effect.type
                    delay(3000.milliseconds)
                    topSnackbarMessageRes = null
                }
                is DashboardEffect.NavigateToFocusSession -> onNavigateToFocus()
                is DashboardEffect.NavigateToAllTasks -> onViewAllTasks()
                is DashboardEffect.NavigateToAllDeadlines -> onViewAllDeadlines()
                else -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colorScheme.surface.background)
                .padding(paddingValues = innerPadding),
            contentPadding = PaddingValues(vertical = Theme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.xl)
        ) {

            // 1. Profile Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Theme.spacing.xl),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AppAvatar(
                            size = 56.dp,
                            initials = state.userName
                        )

                        Column {
                            Text(
                                text = state.welcomeMessage.ifEmpty {
                                    stringResource(R.string.dashboard_greeting, state.userName)
                                },
                                style = Theme.typography.title.large,
                                fontWeight = FontWeight.Bold,
                                color = Theme.colorScheme.text.primary
                            )
                            Text(
                                text = stringResource(state.greetingSubtext),
                                style = Theme.typography.body.medium,
                                color = Theme.colorScheme.text.secondary
                            )
                        }
                    }

                    AppStreakBadge(
                        streakCount = state.streakCount,
                        isActive = state.isStreakActive
                    )
                }
            }

            // 2. Focus Card Section (DISAPPEARS IF NULL)
            state.todayFocus?.takeIf { it.courseName.isNotBlank() }?.let { focus ->
                item {
                    Box(modifier = Modifier.padding(horizontal = Theme.spacing.xl)) {
                        AppFocusCard(
                            title = stringResource(R.string.dashboard_todays_focus),
                            topic = focus.courseName,
                            duration = focus.durationText,
                            imagePainter = ColorPainter(Color(0xFF1E1E1E)),
                            onStartClick = { viewModel.onEvent(DashboardEvent.OnStartFocusClicked) }
                        )
                    }
                }
            }

            // 3. Goals Section
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = Theme.spacing.xl),
                    horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md)
                ) {
                    state.goals.forEach { goal ->
                        val tintColor = when (goal.goalType) {
                            GoalType.DAILY -> Theme.colorScheme.state.info
                            GoalType.WEEKLY -> Theme.colorScheme.state.success
                            GoalType.MONTHLY -> Theme.colorScheme.brand.primary
                        }
                        AppProgressGoalCard(
                            title = stringResource(goal.titleRes),
                            currentValue = goal.currentValue,
                            totalValue = goal.totalValue,
                            unit = stringResource(goal.unitRes),
                            tintColor = tintColor
                        )
                    }
                }
            }

            // 4. Today's Tasks Section Header
            item {
                AppSectionHeader(
                    title = stringResource(R.string.dashboard_todays_tasks),
                    actionText = if (state.tasks.isNotEmpty()) stringResource(R.string.dashboard_view_all) else "",
                    onAction = { viewModel.onEvent(DashboardEvent.OnViewAllTasksClicked) },
                    modifier = Modifier.padding(horizontal = Theme.spacing.xl)
                )
            }

            // Tasks List OR Empty State
            if (state.tasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Theme.spacing.xl, vertical = Theme.spacing.md),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_tasks_for_today),
                            style = Theme.typography.body.medium,
                            color = Theme.colorScheme.text.secondary
                        )
                    }
                }
            } else {
                items(state.tasks, key = { it.id }) { task ->
                    Box(modifier = Modifier.padding(horizontal = Theme.spacing.xl)) {
                        AppTaskCard(
                            title = task.title,
                            duration = task.duration,
                            priority = task.priority,
                            isCompleted = task.isCompleted,
                            onClick = {
                                onNavigateToStudyRoom(task.id, task.title)
                            }
                        )
                    }
                }
            }

            // 5. Upcoming Deadlines Section Header + List
            item {
                AppSectionHeader(
                    title = stringResource(R.string.dashboard_upcoming_deadlines),
                    actionText = if (state.deadlines.isNotEmpty()) stringResource(R.string.dashboard_view_all) else "",
                    onAction = { viewModel.onEvent(DashboardEvent.OnViewAllDeadlinesClicked) },
                    modifier = Modifier.padding(horizontal = Theme.spacing.xl)
                )
            }

// Deadlines List OR Empty State
            if (state.deadlines.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Theme.spacing.xl, vertical = Theme.spacing.md),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_upcoming_deadlines),
                            style = Theme.typography.body.medium,
                            color = Theme.colorScheme.text.secondary
                        )
                    }
                }
            } else {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = Theme.spacing.xl, vertical = Theme.spacing.xs),
                        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md)
                    ) {
                        state.deadlines.forEach { deadline ->
                            AppDeadlineCard(
                                subject = deadline.subject,
                                taskType = deadline.taskType,
                                timeLeft = deadline.timeLeft,
                                tintColor = if (deadline.isUrgent) Theme.colorScheme.state.error else Theme.colorScheme.state.success
                            )
                        }
                    }
                }
            }

            // 6. AI Suggestion Section
            state.aiSuggestionText?.let { suggestion ->
                item {
                    Box(modifier = Modifier.padding(horizontal = Theme.spacing.xl)) {
                        AppInfoCard(
                            header = stringResource(R.string.dashboard_ai_suggestion),
                            body = suggestion,
                            icon = Icons.Default.WbSunny,
                            tintColor = Theme.colorScheme.state.success
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = topSnackbarMessageRes != null,
            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(Theme.spacing.md)
                .padding(top = 32.dp)
        ) {
            topSnackbarMessageRes?.let { messageRes ->
                AppSnackbarContent(
                    message = stringResource(id = messageRes),
                    type = topSnackbarType ?: AppSnackbarType.Error
                )
            }
        }
    }
}