package com.iti.mongez.data.dtos.tasksdtos

import com.google.gson.annotations.SerializedName

data class UpdateTaskRequestDto(
    @SerializedName("task_completed") val taskCompleted: Boolean,
    @SerializedName("active_spent_time") val activeSpentTime: Int
)
