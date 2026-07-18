package com.iti.mongez.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.designsystem.components.avatar.AppAvatar
import com.iti.mongez.designsystem.components.section.AppSectionHeader
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.screens.dashboard.*
import com.iti.mongez.designsystem.theme.Theme

@Composable
fun DashboardScreen(
    innerPadding: PaddingValues,
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToFocus: () -> Unit,
    onViewAllTasks: () -> Unit,
    onViewAllDeadlines: () -> Unit,
    onShowSnackbar: (String, AppSnackbarType) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = true) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is DashboardEffect.ShowSnackbar -> onShowSnackbar(effect.message, effect.type)
                is DashboardEffect.NavigateToFocusSession -> onNavigateToFocus()
                is DashboardEffect.NavigateToAllTasks -> onViewAllTasks()
                is DashboardEffect.NavigateToAllDeadlines -> onViewAllDeadlines()
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colorScheme.surface.background)
            .padding(paddingValues = innerPadding),
        contentPadding = PaddingValues(vertical = Theme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.xl)
    ) {

        // 1. Profile Header Area Row (Updated to use AppAvatar)
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
                        initials = state.userName // AppAvatar will safely take the first two letters internally
                    )

                    Column {
                        Text(
                            text = "Hi, ${state.userName}",
                            style = Theme.typography.title.large,
                            fontWeight = FontWeight.Bold,
                            color = Theme.colorScheme.text.primary
                        )
                        Text(
                            text = state.greetingSubtext,
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

        // 2. Focus Card Section
        item {
            Box(modifier = Modifier.padding(horizontal = Theme.spacing.xl)) {
                AppFocusCard(
                    title = state.focusTitle,
                    topic = state.focusTopic,
                    duration = state.focusDuration,
                    imagePainter = ColorPainter(Color(0xFF1E1E1E)),
                    onStartClick = { viewModel.onEvent(DashboardEvent.OnStartFocusClicked) }
                )
            }
        }

        // 3. Horizontal Goal Row Section
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
                        title = goal.title,
                        currentValue = goal.currentValue,
                        totalValue = goal.totalValue,
                        unit = goal.unit,
                        tintColor = tintColor
                    )
                }
            }
        }

        // 4. Today's Tasks Section Header + List Column (Updated to use AppSectionHeader)
        item {
            AppSectionHeader(
                title = "Today's Tasks",
                actionText = "View all",
                onAction = { viewModel.onEvent(DashboardEvent.OnViewAllTasksClicked) },
                modifier = Modifier.padding(horizontal = Theme.spacing.xl)
            )
        }

        items(state.tasks, key = { it.id }) { task ->
            Box(modifier = Modifier.padding(horizontal = Theme.spacing.xl)) {
                AppTaskCard(
                    title = task.title,
                    duration = task.duration,
                    priority = task.priority,
                    isCompleted = task.isCompleted,
                    onToggle = { isChecked ->
                        viewModel.onEvent(DashboardEvent.OnTaskCheckedToggled(task.id, isChecked))
                    }
                )
            }
        }

        // 5. Upcoming Deadlines Section Header + Horizontal Row Block (Updated to use AppSectionHeader)
        item {
            AppSectionHeader(
                title = "Upcoming Deadlines",
                actionText = "View all",
                onAction = { viewModel.onEvent(DashboardEvent.OnViewAllDeadlinesClicked) },
                modifier = Modifier.padding(horizontal = Theme.spacing.xl)
            )

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

        // 6. Bottom AI Prompt Advice Footnote Card Component
        item {
            Box(modifier = Modifier.padding(horizontal = Theme.spacing.xl)) {
                AppInfoCard(
                    header = state.aiSuggestionHeader,
                    body = state.aiSuggestionBody,
                    icon = Icons.Default.WbSunny,
                    tintColor = Theme.colorScheme.state.success
                )
            }
        }
    }
}