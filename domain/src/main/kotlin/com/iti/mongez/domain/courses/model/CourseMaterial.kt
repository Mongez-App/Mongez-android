package com.iti.mongez.domain.courses.model

import java.time.Instant

data class CourseMaterial(
    val id: String,
    val name: String,
    val pageCount: Int,
    val fileSizeMb: Double,
    val status: String,
    val uploadedAt: Instant,
    val deviceFileUri: String? = null
)
