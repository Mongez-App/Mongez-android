package com.iti.mongez.data.dashboard.repository


import com.iti.mongez.data.dashboard.remote.DashboardApiService
import com.iti.mongez.data.dashboard.remote.toDomain
import com.iti.mongez.domain.core.Result
import com.iti.mongez.data.core.network.safeApi
import com.iti.mongez.domain.dashboard.model.DashboardSummary
import com.iti.mongez.domain.dashboard.model.UserProfile
import com.iti.mongez.domain.dashboard.repository.DashboardRepository
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val apiService: DashboardApiService
) : DashboardRepository {

    override suspend fun getUserProfile(): Result<UserProfile> = safeApi {
        apiService.getUserProfile().toDomain()
    }

    override suspend fun getHomeDashboard(): Result<DashboardSummary> = safeApi {
        apiService.getHomeDashboard().toDomain()
    }
}