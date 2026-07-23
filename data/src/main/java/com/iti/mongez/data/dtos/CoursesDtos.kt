package com.iti.mongez.data.dtos

import com.google.gson.annotations.SerializedName

// --- Requests ---
data class CreateCourseRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("course_code") val courseCode: String,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("exam_date") val examDate: String,
    @SerializedName("has_materials") val hasMaterials: Boolean
)

data class UpdateCourseRequestDto(
    @SerializedName("name") val name: String,
    @SerializedName("is_hidden") val isHidden: Boolean
)

data class MaterialUploadRequestDto(
    @SerializedName("file_name") val fileName: String,
    @SerializedName("content_type") val contentType: String,
    @SerializedName("file_size_bytes") val fileSizeBytes: Long,
    @SerializedName("page_count") val pageCount: Int
)

// --- Responses ---
data class CourseDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("course_code") val courseCode: String?,
    @SerializedName("start_date") val startDate: String?,
    @SerializedName("exam_date") val examDate: String?,
    @SerializedName("has_materials") val hasMaterials: Boolean?,
    @SerializedName("is_hidden") val isHidden: Boolean?,
    @SerializedName("completion_percentage") val completionPercentage: Float?
)

data class CourseCreationResponseDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("course_code") val courseCode: String?,
    @SerializedName("start_date") val startDate: String?,
    @SerializedName("exam_date") val examDate: String?,
    @SerializedName("has_materials") val hasMaterials: Boolean?,
    @SerializedName("completion_percentage") val completionPercentage: Float?,
    @SerializedName("alert") val alert: AlertDto?
)

data class AlertDto(
    @SerializedName("message") val message: String?
)

data class CourseMaterialDto(
    // FIX: Catch both naming conventions to prevent Null/Blank IDs
    @SerializedName(value = "id", alternate = ["material_id"]) val materialId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("page_count") val pageCount: Int?,
    @SerializedName("file_size_mb") val fileSizeMb: Double?,
    @SerializedName("status") val status: String?,
    @SerializedName("uploaded_at") val uploadedAt: String?
)

data class ActionStatusResponseDto(
    @SerializedName("status") val status: String?,
    @SerializedName("alert") val alert: AlertDto?
)

data class MaterialUploadResponseDto(
    @SerializedName("material_id") val materialId: String?,
    @SerializedName("upload_url") val uploadUrl: String?,
    @SerializedName("alert") val alert: AlertDto?
)