package com.iti.mongez.presentation.dashboard

import androidx.annotation.StringRes
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.screens.dashboard.TaskPriority
import com.iti.mongez.presentation.R

data class FocusItem(
    val courseId: String,
    val courseName: String,
    val durationText: String
)

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

data class DashboardUiState(
    val isLoading: Boolean = false,
    val userName: String = "Abdullah",
    val avatarUrl: String? = null,
    val welcomeMessage: String = "",
    @StringRes val greetingSubtext: Int = R.string.dashboard_greeting_subtext,
    val streakCount: Int = 0,
    val isStreakActive: Boolean = false,
    val todayFocus: FocusItem? = null, // Nullable to control visibility
    val goals: List<GoalItem> = emptyList(),
    val tasks: List<TaskItem> = emptyList(),
    val deadlines: List<DeadlineItem> = emptyList(),
    val aiSuggestionText: String? = null
)

sealed class DashboardEvent {
    data class OnTaskCheckedToggled(val taskId: String, val isCompleted: Boolean) : DashboardEvent()
    object OnStartFocusClicked : DashboardEvent()
    object OnViewAllTasksClicked : DashboardEvent()
    object OnViewAllDeadlinesClicked : DashboardEvent()
}

sealed class DashboardEffect {
    data class ShowSnackbar(@StringRes val messageRes: Int, val type: AppSnackbarType) : DashboardEffect()
    data class ShowErrorSnackbar(val message: String) : DashboardEffect()
    object NavigateToFocusSession : DashboardEffect()
    object NavigateToAllTasks : DashboardEffect()
    object NavigateToAllDeadlines : DashboardEffect()
}