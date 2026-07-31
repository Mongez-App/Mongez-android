package com.iti.mongez.data.dtos.coursesdtos

import com.google.gson.annotations.SerializedName

data class CreateCourseRequestDto(
    val name: String,

    @SerializedName("course_code")
    val courseCode: String,

    @SerializedName("image_url")
    val imageUrl: String,

    @SerializedName("start_date")
    val startDate: String,

    @SerializedName("exam_date")
    val examDate: String,

    @SerializedName("course_type")
    val courseType: String,

    @SerializedName("material_url")
    val materialUrl: String? = null
)