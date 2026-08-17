package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.CloudinaryResponseDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface CloudinaryApiService {
    @Multipart
    @POST
    suspend fun uploadImage(
        @Url url: String = "https://api.cloudinary.com/v1_1/vllwannu/image/upload",
        @Part("upload_preset") uploadPreset: RequestBody,
        @Part file: MultipartBody.Part
    ): CloudinaryResponseDto
}