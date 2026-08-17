package com.iti.mongez.domain.core.repository

import com.iti.mongez.domain.core.Result

interface ImageRepository {
    suspend fun uploadImage(imageBytes: ByteArray, fileName: String = "course_image.jpg"): Result<String>
}