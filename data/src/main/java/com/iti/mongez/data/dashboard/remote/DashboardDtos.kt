package com.iti.mongez.data.dashboard.remote

import com.google.gson.annotations.SerializedName
import com.iti.mongez.domain.dashboard.model.*

// --- Profile DTOs ---
data class ProfileResponseDto(
    @SerializedName("stats") val stats: ProfileStatsDto?
)

data class ProfileStatsDto(
    @SerializedName("current_streak_days") val currentStreakDays: Int?
)

fun ProfileResponseDto.toDomain(): UserProfile {
    return UserProfile(
        currentStreakDays = this.stats?.currentStreakDays ?: 0
    )
}

// --- Dashboard DTOs ---
data class DashboardResponseDto(
    @SerializedName("today_focus") val todayFocus: TodayFocusDto?,
    @SerializedName("progress_metrics") val progressMetrics: ProgressMetricsDto?,
    @SerializedName("today_tasks") val todayTasks: List<TodayTaskDto>?,
    @SerializedName("upcoming_deadlines") val upcomingDeadlines: List<UpcomingDeadlineDto>?
)

data class TodayFocusDto(
    @SerializedName("course_name") val courseName: String?,
    @SerializedName("duration_minutes") val durationMinutes: Int?
)

data class ProgressMetricsDto(
    @SerializedName("today_completed_tasks") val todayCompletedTasks: Int?,
    @SerializedName("today_total_tasks") val todayTotalTasks: Int?,
    @SerializedName("weekly_hours_completed") val weeklyHoursCompleted: Int?,
    @SerializedName("weekly_hours_goal") val weeklyHoursGoal: Int?,
    @SerializedName("monthly_hours_completed") val monthlyHoursCompleted: Int?,
    @SerializedName("monthly_hours_goal") val monthlyHoursGoal: Int?
)

data class TodayTaskDto(
    @SerializedName("task_id") val taskId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("duration_minutes") val durationMinutes: Int?,
    @SerializedName("priority") val priority: String?,
    @SerializedName("is_completed") val isCompleted: Boolean?
)

data class UpcomingDeadlineDto(
    @SerializedName("deadline_id") val deadlineId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("course_name") val courseName: String?,
    @SerializedName("due_text") val dueText: String?
)

// --- Domain Mappers ---
fun DashboardResponseDto.toDomain(): DashboardSummary {
    return DashboardSummary(
        focus = this.todayFocus?.let {
            DashboardFocus(
                courseName = it.courseName.orEmpty(),
                durationMinutes = it.durationMinutes ?: 0
            )
        },
        metrics = DashboardMetrics(
            todayCompletedTasks = this.progressMetrics?.todayCompletedTasks ?: 0,
            todayTotalTasks = this.progressMetrics?.todayTotalTasks ?: 0,
            weeklyHoursCompleted = this.progressMetrics?.weeklyHoursCompleted ?: 0,
            weeklyHoursGoal = this.progressMetrics?.weeklyHoursGoal ?: 0,
            monthlyHoursCompleted = this.progressMetrics?.monthlyHoursCompleted ?: 0,
            monthlyHoursGoal = this.progressMetrics?.monthlyHoursGoal ?: 0
        ),
        tasks = this.todayTasks?.map { it.toDomain() }.orEmpty(),
        deadlines = this.upcomingDeadlines?.map { it.toDomain() }.orEmpty()
    )
}

private fun TodayTaskDto.toDomain(): DashboardTask {
    return DashboardTask(
        id = this.taskId.orEmpty(),
        title = this.title.orEmpty(),
        durationMinutes = this.durationMinutes ?: 0,
        priority = this.priority ?: "LOW",
        isCompleted = this.isCompleted ?: false
    )
}

private fun UpcomingDeadlineDto.toDomain(): DashboardDeadline {
    return DashboardDeadline(
        id = this.deadlineId.orEmpty(),
        title = this.title.orEmpty(),
        courseName = this.courseName.orEmpty(),
        dueText = this.dueText.orEmpty()
    )
}