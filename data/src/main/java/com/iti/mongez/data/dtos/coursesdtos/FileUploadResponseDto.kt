package com.iti.mongez.data.dtos.coursesdtos

import com.google.gson.annotations.SerializedName

data class FileUploadResponseDto(
    @SerializedName("material_id") val materialId: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("message") val message: String?
)