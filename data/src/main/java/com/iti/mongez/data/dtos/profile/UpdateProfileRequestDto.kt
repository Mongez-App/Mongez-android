package com.iti.mongez.data.dtos.profile

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequestDto(
    @SerializedName("name") val name: String?,
    @SerializedName("avatar_url") val avatarUrl: String?
)