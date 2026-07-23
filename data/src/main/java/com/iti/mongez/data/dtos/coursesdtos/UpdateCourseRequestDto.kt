package com.iti.mongez.data.dtos.coursesdtos

import com.google.gson.annotations.SerializedName

data class UpdateCourseRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("is_hidden") val isHidden: Boolean
)