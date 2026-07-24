package com.iti.mongez.data.dtos.dashboarddtos

import com.google.gson.annotations.SerializedName

data class UpcomingDeadlineDto(
    @SerializedName("deadline_id") val deadlineId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("course_name") val courseName: String?,
    @SerializedName("due_text") val dueText: String?
)