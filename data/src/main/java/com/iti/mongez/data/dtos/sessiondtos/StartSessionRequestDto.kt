package com.iti.mongez.data.dtos.sessiondtos

import com.google.gson.annotations.SerializedName

data class StartSessionRequestDto(
    @SerializedName("course_id", alternate = ["courseId"]) val courseId: String,
    @SerializedName("estimated_duration_minutes", alternate = ["estimatedDurationMinutes"]) val estimatedDurationMinutes: Int,
    @SerializedName("linked_task_id", alternate = ["linkedTaskId"]) val linkedTaskId: String
)
