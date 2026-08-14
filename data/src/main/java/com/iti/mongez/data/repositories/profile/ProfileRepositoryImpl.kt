package com.iti.mongez.data.repositories.profile

import com.iti.mongez.data.dtos.profile.UpdateProfileRequestDto
import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.data.mapper.toEntity
import com.iti.mongez.data.local.dao.UserDao
import com.iti.mongez.data.sources.remote.FirebaseAuthDataSource
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.profile.model.Profile
import com.iti.mongez.domain.profile.repository.ProfileRepository
import com.iti.mongez.data.sources.remote.interfaces.UserRemoteDataSource
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserRemoteDataSource,
    private val userDao: UserDao,
    private val firebaseAuthDataSource: FirebaseAuthDataSource
) : ProfileRepository {
    override suspend fun getFullProfile(): Result<Profile> = safeApi {
        userRemoteDataSource.getFullUserProfile().toDomain()
    }

    override suspend fun updateProfile(
        name: String?,
        avatarUrl: String?,
        appearance: String?,
        language: String?
    ): Result<Profile> = safeApi {
        val request = UpdateProfileRequestDto(
            name = name,
            avatarUrl = avatarUrl,
            appearance = appearance,
            language = language
        )
        val response = userRemoteDataSource.updateFullUserProfile(request)
        val profile = response.profile?.toDomain() ?: throw Exception("Profile update failed")
        
        // Sync with local database
        userDao.getUser()?.let { userEntity ->
            val updatedUser = userEntity.toDomain().copy(
                name = profile.name ?: userEntity.name,
                avatarUrl = profile.avatarUrl ?: userEntity.avatarUrl
            )
            userDao.insertUser(updatedUser.toEntity())
        }

        // Sync with Firebase
        name?.let { firebaseAuthDataSource.updateDisplayName(it) }

        profile
    }

    override suspend fun uploadProfileImage(imageBytes: ByteArray): Result<String> {
        return safeApi {
            val mediaType = MediaType.parse("image/jpeg")
            val requestFile = RequestBody.create(mediaType, imageBytes)

            val multipartBody = MultipartBody.Part.createFormData("avatar", "profile_image.jpg", requestFile)

            val response = userRemoteDataSource.uploadProfileImage(multipartBody)

            response.profile?.avatarUrl ?: throw Exception("Server did not return a valid avatar URL")
        }
    }
}
