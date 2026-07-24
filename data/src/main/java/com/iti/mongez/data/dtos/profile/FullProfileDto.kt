package com.iti.mongez.data.dtos.profile

import com.google.gson.annotations.SerializedName

data class FullProfileDto(
    @SerializedName("user_id") val userId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("stats") val stats: FullProfileStatsDto?
)
