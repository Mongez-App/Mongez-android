package com.iti.mongez.domain.profile.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.profile.model.Profile
import com.iti.mongez.domain.profile.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        name: String? = null,
        avatarUrl: String? = null,
        appearance: String? = null,
        language: String? = null
    ): Result<Profile> = repository.updateProfile(
        name = name,
        avatarUrl = avatarUrl,
        appearance = appearance,
        language = language
    )
}
