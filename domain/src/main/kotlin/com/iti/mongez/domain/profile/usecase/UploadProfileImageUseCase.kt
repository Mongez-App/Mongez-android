package com.iti.mongez.domain.profile.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.profile.repository.ProfileRepository
import javax.inject.Inject

class UploadProfileImageUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(imageBytes: ByteArray): Result<String> {
        return repository.uploadProfileImage(imageBytes)
    }
}