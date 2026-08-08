package com.iti.mongez.domain.dashboard.model

data class UserProfile(
    val name: String,
    val avatarUrl: String?,
    val currentStreakDays: Int
)

data class DashboardFocus(
    val courseId: String,
    val courseName: String,
    val allocatedDuration: String,
    val durationMinutes: Int
)

data class DashboardMetrics(
    val todayCompletedTasks: Int,
    val todayTotalTasks: Int,
    val weeklyHoursCompleted: Int,
    val weeklyHoursGoal: Int,
    val monthlyHoursCompleted: Int,
    val monthlyHoursGoal: Int
)

data class DashboardTask(
    val id: String,
    val title: String,
    val durationMinutes: Int,
    val priority: String, // "HIGH", "MEDIUM", "LOW"
    val isCompleted: Boolean
)

data class DashboardDeadline(
    val id: String,
    val title: String,
    val courseName: String,
    val dueText: String
)

data class DashboardSummary(
    val welcomeMessage: String,
    val focus: DashboardFocus?,
    val metrics: DashboardMetrics,
    val tasks: List<DashboardTask>,
    val deadlines: List<DashboardDeadline>,
    val streakDays: Int,
    val aiSuggestion: String?
)

// The combined result to pass to the Presentation Layer
data class DashboardAggregatedData(
    val userName: String,
    val avatarUrl: String?,
    val streak: Int,
    val summary: DashboardSummary
)