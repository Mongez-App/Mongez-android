package com.iti.mongez.data.dtos.sessiondtos

import com.google.gson.annotations.SerializedName

data class EndSessionResponseDto(
    @SerializedName("session_id") val sessionId: String?,
    @SerializedName("course_id") val courseId: String?,
    @SerializedName("linked_task_id") val linkedTaskId: String?,
    @SerializedName("started_at") val startedAt: String?,
    @SerializedName("duration_minutes_logged") val durationMinutesLogged: Int?,
    @SerializedName("task_completed") val taskCompleted: Boolean?,
    @SerializedName("completion_time") val completionTime: String?,
    @SerializedName("alert") val alert: SessionAlertDto?
)

data class SessionAlertDto(
    @SerializedName("message") val message: String?
)
