package com.iti.mongez.domain.profile.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.profile.model.Profile
import com.iti.mongez.domain.profile.repository.ProfileRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<Profile> = repository.getFullProfile()
}
