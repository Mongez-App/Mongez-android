package com.iti.mongez.data.dtos.sessiondtos

import com.google.gson.annotations.SerializedName

data class StartSessionResponseDto(
    @SerializedName("session_id") val sessionId: String?,
    @SerializedName("course_id") val courseId: String?,
    @SerializedName("linked_task_id") val linkedTaskId: String?,
    @SerializedName("started_at") val startedAt: String?
)
