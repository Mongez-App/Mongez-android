package com.iti.mongez.data.dtos.dashboarddtos

import com.google.gson.annotations.SerializedName

data class ProfileResponseDto(
    @SerializedName("name") val name: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?,
    @SerializedName("stats") val stats: ProfileStatsDto?
)