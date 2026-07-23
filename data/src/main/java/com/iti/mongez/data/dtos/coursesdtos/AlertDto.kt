package com.iti.mongez.data.dtos.coursesdtos

import com.google.gson.annotations.SerializedName

data class AlertDto(
    @SerializedName("message") val message: String?
)