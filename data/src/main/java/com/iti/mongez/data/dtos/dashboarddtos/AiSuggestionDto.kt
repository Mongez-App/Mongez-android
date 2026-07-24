package com.iti.mongez.data.dtos.dashboarddtos

import com.google.gson.annotations.SerializedName

data class AiSuggestionDto(
    @SerializedName("text") val text: String?
)