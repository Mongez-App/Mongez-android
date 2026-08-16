package com.iti.mongez.data.dtos.dashboarddtos

import com.google.gson.annotations.SerializedName

data class TodayTaskDto(
    @SerializedName("id", alternate = ["taskId", "task_id"]) val taskId: String?,
    @SerializedName("course_id", alternate = ["courseId"]) val courseId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("duration_minutes", alternate = ["durationMinutes"]) val durationMinutes: Int?,
    @SerializedName("priority") val priority: String?,
    @SerializedName("completed", alternate = ["isCompleted", "is_completed"]) val isCompleted: Boolean?
)