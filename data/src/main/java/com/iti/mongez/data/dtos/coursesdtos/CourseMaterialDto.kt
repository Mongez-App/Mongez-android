package com.iti.mongez.data.dtos.coursesdtos

import com.google.gson.annotations.SerializedName

data class CourseMaterialDto(
    @SerializedName(value = "id", alternate = ["material_id"]) val materialId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("page_count") val pageCount: Int?,
    @SerializedName("file_size_mb") val fileSizeMb: Double?,
    @SerializedName("status") val status: String?,
    @SerializedName("uploaded_at") val uploadedAt: String?,
    @SerializedName("device_file_uri") val deviceFileUri: String?
)