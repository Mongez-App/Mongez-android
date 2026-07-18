package com.iti.mongez.presentation.dashboard

import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.screens.dashboard.TaskPriority

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
    val title: String,
    val currentValue: Int,
    val totalValue: Int,
    val unit: String,
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
    val greetingSubtext: String = "Let's hit today's tasks",
    val streakCount: Int = 12,
    val isStreakActive: Boolean = true,
    val focusTitle: String = "Today's Focus",
    val focusTopic: String = "Operating\nSystems",
    val focusDuration: String = "2h 15m",
    val goals: List<GoalItem> = emptyList(),
    val tasks: List<TaskItem> = emptyList(),
    val deadlines: List<DeadlineItem> = emptyList(),
    val aiSuggestionHeader: String = "AI Suggestion",
    val aiSuggestionBody: String = "You're most productive around 7 PM. Start Networking before Algorithms today."
)

sealed class DashboardEvent {
    data class OnTaskCheckedToggled(val taskId: String, val isCompleted: Boolean) : DashboardEvent()
    object OnStartFocusClicked : DashboardEvent()
    object OnViewAllTasksClicked : DashboardEvent()
    object OnViewAllDeadlinesClicked : DashboardEvent()
}

sealed class DashboardEffect {
    data class ShowSnackbar(val message: String, val type: AppSnackbarType) : DashboardEffect()    object NavigateToFocusSession : DashboardEffect()
    object NavigateToAllTasks : DashboardEffect()
    object NavigateToAllDeadlines : DashboardEffect()
}