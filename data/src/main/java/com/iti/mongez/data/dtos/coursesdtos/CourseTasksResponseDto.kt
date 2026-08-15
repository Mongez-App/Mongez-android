package com.iti.mongez.data.dtos.coursesdtos

import com.google.gson.annotations.SerializedName

data class CourseTasksResponseDto(
    @SerializedName("meta") val meta: CourseTasksMetaDto?,
    @SerializedName("data") val data: List<CourseTaskDto>?
)

data class CourseTasksMetaDto(
    @SerializedName("preferred_study_time_minutes") val preferredStudyTimeMinutes: Int?,
    @SerializedName("total_tasks") val totalTasks: Int?
)
