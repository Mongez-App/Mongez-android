package com.iti.mongez.data.dtos.coursesdtos

import com.google.gson.annotations.SerializedName

data class ActionStatusResponseDto(
    @SerializedName("status") val status: String?,
    @SerializedName("alert") val alert: AlertDto?
)