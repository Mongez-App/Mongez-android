package com.iti.mongez.data.dtos

import com.google.gson.annotations.SerializedName
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.courses.model.CourseCreationResult

data class CourseDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("course_code") val courseCode: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("exam_date") val examDate: String,
    @SerializedName("has_materials") val hasMaterials: Boolean,
    @SerializedName("completion_percentage") val completionPercentage: Float
)

data class CreateCourseRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("course_code") val courseCode: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("exam_date") val examDate: String,
    @SerializedName("has_materials") val hasMaterials: Boolean
)

data class CreateCourseResponseDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("course_code") val courseCode: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("exam_date") val examDate: String,
    @SerializedName("has_materials") val hasMaterials: Boolean,
    @SerializedName("completion_percentage") val completionPercentage: Float,
    @SerializedName("alert") val alert: AlertDto?
)

data class AlertDto(
    @SerializedName("message") val message: String
)