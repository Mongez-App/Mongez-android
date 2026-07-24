package com.iti.mongez.data.dtos.dashboarddtos

import com.google.gson.annotations.SerializedName

data class TodayTaskDto(
    @SerializedName("task_id") val taskId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("duration_minutes") val durationMinutes: Int?,
    @SerializedName("priority") val priority: String?,
    @SerializedName("is_completed") val isCompleted: Boolean?
)