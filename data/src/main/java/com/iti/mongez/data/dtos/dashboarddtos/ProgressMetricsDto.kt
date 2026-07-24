package com.iti.mongez.data.dtos.dashboarddtos

import com.google.gson.annotations.SerializedName

data class ProgressMetricsDto(
    @SerializedName("today_completed_tasks") val todayCompletedTasks: Int?,
    @SerializedName("today_total_tasks") val todayTotalTasks: Int?,
    @SerializedName("weekly_hours_completed") val weeklyHoursCompleted: Int?,
    @SerializedName("weekly_hours_goal") val weeklyHoursGoal: Int?,
    @SerializedName("monthly_hours_completed") val monthlyHoursCompleted: Int?,
    @SerializedName("monthly_hours_goal") val monthlyHoursGoal: Int?
)