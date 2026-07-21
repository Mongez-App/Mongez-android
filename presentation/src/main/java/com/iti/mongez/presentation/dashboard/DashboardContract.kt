package com.iti.mongez.presentation.dashboard

import androidx.annotation.StringRes
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.screens.dashboard.TaskPriority
import com.iti.mongez.presentation.R

// Represent individual domain models inline for presentation state driving
data class TaskItem(
    val id: String,
    val title: String,
    val duration: String,
    val priority: TaskPriority,
    val isCompleted: Boolean
)

data class DeadlineItem(
    val id: String,
    val subject: String,
    val taskType: String,
    val timeLeft: String,
    val isUrgent: Boolean
)

data class GoalItem(
    @StringRes val titleRes: Int,
    val currentValue: Int,
    val totalValue: Int,
    @StringRes val unitRes: Int,
    val goalType: GoalType
)

enum class GoalType {
    DAILY, WEEKLY, MONTHLY
}

// ────────────────────────────────────────────────────────────────
// MVI Contract Definitons
// ────────────────────────────────────────────────────────────────

data class DashboardUiState(
    val isLoading: Boolean = false,
    val userName: String = "Abdullah",
    @StringRes val greetingSubtext: Int = R.string.dashboard_greeting_subtext,
    val streakCount: Int = 12,
    val isStreakActive: Boolean = true,
    @StringRes val focusTitle: Int = R.string.dashboard_todays_focus,
    @StringRes val focusTopic: Int = R.string.dashboard_focus_topic,
    @StringRes val focusDuration: Int = R.string.dashboard_focus_duration,
    val goals: List<GoalItem> = emptyList(),
    val tasks: List<TaskItem> = emptyList(),
    val deadlines: List<DeadlineItem> = emptyList(),
    @StringRes val aiSuggestionHeader: Int = R.string.dashboard_ai_suggestion,
    @StringRes val aiSuggestionBody: Int = R.string.dashboard_ai_suggestion_body
)

sealed class DashboardEvent {
    data class OnTaskCheckedToggled(val taskId: String, val isCompleted: Boolean) : DashboardEvent()
    object OnStartFocusClicked : DashboardEvent()
    object OnViewAllTasksClicked : DashboardEvent()
    object OnViewAllDeadlinesClicked : DashboardEvent()
}

sealed class DashboardEffect {
    data class ShowSnackbar(@StringRes val messageRes: Int, val type: AppSnackbarType) : DashboardEffect()
    object NavigateToFocusSession : DashboardEffect()
    object NavigateToAllTasks : DashboardEffect()
    object NavigateToAllDeadlines : DashboardEffect()
}