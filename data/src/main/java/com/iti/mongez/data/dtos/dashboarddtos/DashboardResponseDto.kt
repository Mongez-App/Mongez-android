package com.iti.mongez.data.dtos.dashboarddtos

import com.google.gson.annotations.SerializedName

data class DashboardResponseDto(
    @SerializedName("welcome_message") val welcomeMessage: String?,
    @SerializedName("today_focus") val todayFocus: TodayFocusDto?,
    @SerializedName("progress_metrics") val progressMetrics: ProgressMetricsDto?,
    @SerializedName("today_tasks") val todayTasks: List<TodayTaskDto>?,
    @SerializedName("upcoming_deadlines") val upcomingDeadlines: List<UpcomingDeadlineDto>?,
    @SerializedName("streak") val streak: StreakDto?,
    @SerializedName("ai_suggestion") val aiSuggestion: AiSuggestionDto?
)