package com.iti.mongez.domain.core.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.core.repository.ImageRepository
import javax.inject.Inject

class UploadImageUseCase @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(imageBytes: ByteArray, fileName: String = "course_image.jpg"): Result<String> {
        return imageRepository.uploadImage(imageBytes, fileName)
    }
}
