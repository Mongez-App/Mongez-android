package com.iti.mongez.data.repositories.image

import com.iti.mongez.data.sources.remote.services.CloudinaryApiService
import com.iti.mongez.domain.core.repository.ImageRepository
import com.iti.mongez.domain.core.Result
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val cloudinaryApiService: CloudinaryApiService
) : ImageRepository {
    override suspend fun uploadImage(imageBytes: ByteArray, fileName: String): Result<String> {
        return try {
            val mediaTypeText = MediaType.parse("text/plain")
            val uploadPreset = RequestBody.create(mediaTypeText, "Mongez")
            
            val mediaTypeImage = MediaType.parse("image/jpeg")
            val requestBody = RequestBody.create(mediaTypeImage, imageBytes)
            
            val multipartBody = MultipartBody.Part.createFormData("file", fileName, requestBody)

            val response = cloudinaryApiService.uploadImage(
                url = "https://api.cloudinary.com/v1_1/vllwannu/image/upload",
                uploadPreset = uploadPreset,
                file = multipartBody
            )

            if (response.secureUrl != null) {
                Result.Success(response.secureUrl)
            } else {
                Result.Failure(Exception("Cloudinary upload failed: Secure URL is null"))
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
    }
}
