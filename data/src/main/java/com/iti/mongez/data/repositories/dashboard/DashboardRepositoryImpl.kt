package com.iti.mongez.data.repositories.dashboard

import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.services.ApiService
import com.iti.mongez.data.dtos.toDomain
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.dashboard.model.DashboardSummary
import com.iti.mongez.domain.dashboard.model.UserProfile
import com.iti.mongez.domain.dashboard.repository.DashboardRepository
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : DashboardRepository {

    override suspend fun getUserProfile(): Result<UserProfile> = safeApi {
        apiService.getUserProfile().toDomain()
    }

    override suspend fun getHomeDashboard(): Result<DashboardSummary> = safeApi {
        apiService.getHomeDashboard().toDomain()
    }
}