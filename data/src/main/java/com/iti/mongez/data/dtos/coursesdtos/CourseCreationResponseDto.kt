package com.iti.mongez.data.dtos.coursesdtos

import com.google.gson.annotations.SerializedName

data class CourseCreationResponseDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("course_code") val courseCode: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("start_date") val startDate: String?,
    @SerializedName("exam_date") val examDate: String?,
    @SerializedName("has_materials") val hasMaterials: Boolean?,
    @SerializedName("completion_percentage") val completionPercentage: Float?,
    @SerializedName("alert") val alert: AlertDto?
)