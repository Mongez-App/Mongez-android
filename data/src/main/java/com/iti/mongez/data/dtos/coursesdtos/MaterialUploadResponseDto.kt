package com.iti.mongez.data.dtos.coursesdtos

import com.google.gson.annotations.SerializedName

data class MaterialUploadResponseDto(
    @SerializedName("material_id") val materialId: String?,
    @SerializedName("upload_url") val uploadUrl: String?,
    @SerializedName("alert") val alert: AlertDto?
)