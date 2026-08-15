package com.iti.mongez.data.dtos.sessiondtos

import com.google.gson.annotations.SerializedName

data class EndSessionRequestDto(
    @SerializedName("task_completed", alternate = ["is_completed", "isCompleted", "taskCompleted"]) val taskCompleted: Boolean,
    @SerializedName("completion_time", alternate = ["completionTime"]) val completionTime: String
)
