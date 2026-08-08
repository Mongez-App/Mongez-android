package com.iti.mongez.data.mapper

import com.iti.mongez.data.dtos.dashboarddtos.DashboardResponseDto
import com.iti.mongez.data.dtos.dashboarddtos.ProfileResponseDto
import com.iti.mongez.data.dtos.dashboarddtos.TodayTaskDto
import com.iti.mongez.data.dtos.dashboarddtos.UpcomingDeadlineDto
import com.iti.mongez.domain.dashboard.model.DashboardDeadline
import com.iti.mongez.domain.dashboard.model.DashboardFocus
import com.iti.mongez.domain.dashboard.model.DashboardMetrics
import com.iti.mongez.domain.dashboard.model.DashboardSummary
import com.iti.mongez.domain.dashboard.model.DashboardTask
import com.iti.mongez.domain.dashboard.model.UserProfile

fun ProfileResponseDto.toDomain(): UserProfile {
    return UserProfile(
        name = this.name ?: "",
        avatarUrl = this.avatarUrl,
        currentStreakDays = this.stats?.currentStreakDays ?: 0
    )
}
fun DashboardResponseDto.toDomain(): DashboardSummary {
    return DashboardSummary(
        welcomeMessage = this.welcomeMessage.orEmpty(),
        focus = this.todayFocus?.takeIf { !it.courseName.isNullOrBlank() }?.let {
            DashboardFocus(
                courseId = it.courseId.orEmpty(),
                courseName = it.courseName.orEmpty(),
                allocatedDuration = it.allocatedDuration.orEmpty(),
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
        deadlines = this.upcomingDeadlines?.map { it.toDomain() }.orEmpty(),
        streakDays = this.streak?.currentStreakDays ?: 0,
        aiSuggestion = this.aiSuggestion?.text
    )
}

fun TodayTaskDto.toDomain(): DashboardTask {
    return DashboardTask(
        id = this.taskId.orEmpty(),
        title = this.title.orEmpty(),
        durationMinutes = this.durationMinutes ?: 0,
        priority = this.priority ?: "LOW",
        isCompleted = this.isCompleted ?: false
    )
}

fun UpcomingDeadlineDto.toDomain(): DashboardDeadline {
    return DashboardDeadline(
        id = this.deadlineId.orEmpty(),
        title = this.title.orEmpty(),
        courseName = this.courseName.orEmpty(),
        dueText = this.dueText.orEmpty()
    )
}