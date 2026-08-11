package com.iti.mongez.data.dtos.coursesdtos

import com.google.gson.annotations.SerializedName

data class MaterialUploadRequestDto(
    @SerializedName("file_name") val fileName: String,
    @SerializedName("content_type") val contentType: String,
    @SerializedName("file_size_bytes") val fileSizeBytes: Long,
    @SerializedName("page_count") val pageCount: Int,
    @SerializedName("device_file_uri") val deviceFileUri: String? = null
)
