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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.iti.mongez.designsystem.components.avatar.AppAvatar
import com.iti.mongez.designsystem.components.loading.AppShimmer
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
    onViewAllDeadlines: () -> Unit,
    onNavigateToStudyRoom: (String, String) -> Unit,
    onViewAllTasks: (List<TaskItem>) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    var topSnackbarMessageRes by remember { mutableStateOf<Int?>(null) }
    var topSnackbarType by remember { mutableStateOf<AppSnackbarType?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshData()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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
                is DashboardEffect.NavigateToAllTasks -> onViewAllTasks(state.tasks)
                is DashboardEffect.NavigateToAllDeadlines -> onViewAllDeadlines()
                else -> {}
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Theme.colorScheme.surface.background)) {

        if (state.isLoading) {
            DashboardShimmerLoading(innerPadding = innerPadding)
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = innerPadding),
                contentPadding = PaddingValues(vertical = Theme.spacing.xl),
                verticalArrangement = Arrangement.spacedBy(Theme.spacing.xl)
            ) {

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Theme.spacing.xl),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AppAvatar(
                                size = 56.dp,
                                imageUrl = state.avatarUrl,
                                initials = state.userName
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                // Removed the welcome message line & Text Truncation to let long names wrap natively
                                Text(
                                    text = state.userName,
                                    style = Theme.typography.title.large,
                                    fontWeight = FontWeight.Bold,
                                    color = Theme.colorScheme.text.primary
                                )

                                Text(
                                    text = stringResource(state.greetingSubtext),
                                    style = Theme.typography.body.small,
                                    color = Theme.colorScheme.text.secondary,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(Theme.spacing.sm))

                        AppStreakBadge(
                            streakCount = state.streakCount,
                            isActive = state.isStreakActive
                        )
                    }
                }

                state.todayFocus?.takeIf { it.courseName.isNotBlank() }?.let { focus ->
                    item {
                        Box(modifier = Modifier.padding(horizontal = Theme.spacing.xl)) {
                            AppFocusCard(
                                title = stringResource(R.string.dashboard_todays_focus),
                                topic = focus.courseName,
                                duration = focus.durationText,
                                imagePainter = null, // Will automatically display initials like "DS" on translucent background
                                onStartClick = { viewModel.onEvent(DashboardEvent.OnStartFocusClicked) }
                            )
                        }
                    }
                }

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

                val tasksToShow = state.tasks.take(3)

                item {
                    AppSectionHeader(
                        title = stringResource(R.string.dashboard_todays_tasks),
                        actionText = if (state.tasks.size > tasksToShow.size) stringResource(R.string.dashboard_view_all) else "",
                        onAction = { viewModel.onEvent(DashboardEvent.OnViewAllTasksClicked) },
                        modifier = Modifier.padding(horizontal = Theme.spacing.xl)
                    )
                }

                if (tasksToShow.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = Theme.spacing.xl, vertical = Theme.spacing.md),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_no_tasks),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(135.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = stringResource(R.string.no_tasks_for_today),
                                style = Theme.typography.body.medium,
                                color = Theme.colorScheme.text.secondary
                            )
                        }
                    }
                } else {
                    items(tasksToShow, key = { it.id }) { task ->
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

                item {
                    AppSectionHeader(
                        title = stringResource(R.string.dashboard_upcoming_deadlines),
                        actionText = if (state.deadlines.isNotEmpty()) stringResource(R.string.dashboard_view_all) else "",
                        onAction = { viewModel.onEvent(DashboardEvent.OnViewAllDeadlinesClicked) },
                        modifier = Modifier.padding(horizontal = Theme.spacing.xl)
                    )
                }

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

@Composable
fun DashboardShimmerLoading(innerPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(vertical = Theme.spacing.xl),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.xl)
    ) {
        // Profile Header Shimmer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.spacing.xl),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AppShimmer(modifier = Modifier.size(56.dp).clip(RoundedCornerShape(28.dp)))
            Spacer(modifier = Modifier.width(Theme.spacing.md))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.weight(1f)) {
                AppShimmer(height = 24.dp, modifier = Modifier.fillMaxWidth(0.5f))
                AppShimmer(height = 16.dp, modifier = Modifier.fillMaxWidth(0.3f))
            }
            AppShimmer(modifier = Modifier.size(70.dp, 36.dp).clip(RoundedCornerShape(18.dp)))
        }

        // Focus Card Shimmer
        Box(modifier = Modifier.padding(horizontal = Theme.spacing.xl)) {
            AppShimmer(height = 120.dp, modifier = Modifier.fillMaxWidth())
        }

        // Goals Row Shimmer
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Theme.spacing.xl),
            horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            AppShimmer(modifier = Modifier.size(140.dp, 86.dp))
            AppShimmer(modifier = Modifier.size(140.dp, 86.dp))
            AppShimmer(modifier = Modifier.size(140.dp, 86.dp))
        }

        // Section Header Shimmer
        Box(modifier = Modifier.padding(horizontal = Theme.spacing.xl)) {
            AppShimmer(height = 24.dp, modifier = Modifier.fillMaxWidth(0.4f))
        }

        // Tasks Shimmer
        Column(
            modifier = Modifier.padding(horizontal = Theme.spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Theme.spacing.md)
        ) {
            AppShimmer(height = 76.dp, modifier = Modifier.fillMaxWidth())
            AppShimmer(height = 76.dp, modifier = Modifier.fillMaxWidth())
        }
    }
}