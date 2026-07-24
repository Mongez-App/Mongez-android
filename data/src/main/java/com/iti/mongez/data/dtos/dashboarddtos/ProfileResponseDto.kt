package com.iti.mongez.data.dtos.dashboarddtos

import com.google.gson.annotations.SerializedName

data class ProfileResponseDto(
    @SerializedName("stats") val stats: ProfileStatsDto?
)