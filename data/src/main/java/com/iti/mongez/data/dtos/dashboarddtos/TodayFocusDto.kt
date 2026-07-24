package com.iti.mongez.data.dtos.dashboarddtos

import com.google.gson.annotations.SerializedName

data class TodayFocusDto(
    @SerializedName("course_id") val courseId: String?,
    @SerializedName("course_name") val courseName: String?,
    @SerializedName("allocated_duration") val allocatedDuration: String?,
    @SerializedName("duration_minutes") val durationMinutes: Int?
)