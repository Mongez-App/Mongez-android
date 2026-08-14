package com.iti.mongez.data.sources.remote.interfaces

import com.iti.mongez.data.dtos.UserPreferencesDto
import com.iti.mongez.data.dtos.profile.FullProfileDto
import com.iti.mongez.data.dtos.profile.UpdateProfileRequestDto
import com.iti.mongez.data.dtos.profile.UpdateProfileResponseDto
import okhttp3.MultipartBody

interface UserRemoteDataSource {
    suspend fun getFullUserProfile(): FullProfileDto
    suspend fun uploadProfileImage(image: MultipartBody.Part): UpdateProfileResponseDto
    suspend fun updateFullUserProfile(request: UpdateProfileRequestDto): UpdateProfileResponseDto
    suspend fun getPreferences(): UserPreferencesDto
    suspend fun updatePreferences(preferences: UserPreferencesDto)
}
