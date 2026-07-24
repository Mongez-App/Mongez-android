package com.iti.mongez.data.dtos.profile

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponseDto(
    @SerializedName("status") val status: String,
    @SerializedName("profile") val profile: FullProfileDto?
)
