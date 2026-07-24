package com.iti.mongez.data.repositories.profile

import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.services.ApiService
import com.iti.mongez.data.dtos.toDomain
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.profile.model.Profile
import com.iti.mongez.domain.profile.repository.ProfileRepository
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProfileRepository {
    override suspend fun getFullProfile(): Result<Profile> = safeApi {
        apiService.getFullUserProfile().toDomain()
    }
}
