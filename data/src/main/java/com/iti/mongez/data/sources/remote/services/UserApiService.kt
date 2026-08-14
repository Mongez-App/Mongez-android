package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.UserPreferencesDto
import com.iti.mongez.data.dtos.profile.FullProfileDto
import com.iti.mongez.data.dtos.profile.UpdateProfileRequestDto
import com.iti.mongez.data.dtos.profile.UpdateProfileResponseDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface UserApiService {
    @GET("users/me/profile")
    suspend fun getFullUserProfile(): FullProfileDto

    @Multipart
    @POST("profile/avatar")
    suspend fun uploadProfileImage(
        @Part image: MultipartBody.Part
    ): UpdateProfileResponseDto

    @PATCH("users/me/profile")
    suspend fun updateFullUserProfile(@Body request: UpdateProfileRequestDto): UpdateProfileResponseDto

    @GET("users/me/preferences")
    suspend fun getPreferences(): UserPreferencesDto

    @PUT("users/me/preferences")
    suspend fun updatePreferences(@Body preferences: UserPreferencesDto)
}
