package com.iti.mongez.data.sources.remote.impl

import com.iti.mongez.data.dtos.UserPreferencesDto
import com.iti.mongez.data.dtos.profile.FullProfileDto
import com.iti.mongez.data.dtos.profile.UpdateProfileRequestDto
import com.iti.mongez.data.dtos.profile.UpdateProfileResponseDto
import com.iti.mongez.data.sources.remote.interfaces.UserRemoteDataSource
import com.iti.mongez.data.sources.remote.services.UserApiService
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userApiService: UserApiService
) : UserRemoteDataSource {
    override suspend fun getFullUserProfile(): FullProfileDto {
        return userApiService.getFullUserProfile()
    }

    override suspend fun uploadProfileImage(image: MultipartBody.Part): UpdateProfileResponseDto {
        return userApiService.uploadProfileImage(image)
    }

    override suspend fun updateFullUserProfile(request: UpdateProfileRequestDto): UpdateProfileResponseDto {
        return userApiService.updateFullUserProfile(request)
    }

    override suspend fun getPreferences(): UserPreferencesDto {
        return userApiService.getPreferences()
    }

    override suspend fun updatePreferences(preferences: UserPreferencesDto) {
        userApiService.updatePreferences(preferences)
    }
}
