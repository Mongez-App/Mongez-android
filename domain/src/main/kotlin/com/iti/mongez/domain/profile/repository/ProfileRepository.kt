package com.iti.mongez.domain.profile.repository

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.profile.model.Profile

interface ProfileRepository {
    suspend fun getFullProfile(): Result<Profile>
}
