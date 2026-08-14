package com.iti.mongez.data.dtos.coursesdtos

import com.google.gson.annotations.SerializedName

data class CourseTaskDto(
    @SerializedName("id") val id: String?,
    @SerializedName("user_id") val userId: String?,
    @SerializedName("course_id") val courseId: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("duration_minutes") val durationMinutes: Int?,
    @SerializedName("active_spent_time") val activeSpentTime: Int?,
    @SerializedName("priority") val priority: String?,
    @SerializedName("completed") val completed: Boolean?,
    @SerializedName("scheduled_date") val scheduledDate: String?,
    @SerializedName("sequence_order") val sequenceOrder: Int?,
    @SerializedName("created_at") val createdAt: String?
)
