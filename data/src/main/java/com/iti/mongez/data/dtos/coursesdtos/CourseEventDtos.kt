package com.iti.mongez.data.dtos.coursesdtos

import com.google.gson.annotations.SerializedName

data class AddEventRequestDto(
    @SerializedName("title") val title: String,
    @SerializedName("event_type") val eventType: String,
    @SerializedName("event_date") val eventDate: String
)

data class AddEventResponseDto(
    @SerializedName("message") val message: String
)
