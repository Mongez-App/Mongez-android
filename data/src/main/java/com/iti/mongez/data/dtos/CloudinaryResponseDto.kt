package com.iti.mongez.data.dtos

import com.google.gson.annotations.SerializedName

data class CloudinaryResponseDto(
    @SerializedName("secure_url") val secureUrl: String?,
    @SerializedName("public_id") val publicId: String?,
    val format: String?,
    @SerializedName("resource_type") val resourceType: String?
)