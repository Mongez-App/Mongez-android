package com.iti.mongez.data.repositories.dashboard

import android.util.Log
import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.services.ApiService
import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.dashboard.model.DashboardSummary
import com.iti.mongez.domain.dashboard.model.UserProfile
import com.iti.mongez.domain.dashboard.repository.DashboardRepository
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : DashboardRepository {

    override suspend fun getUserProfile(): Result<UserProfile> = safeApi {
        val dto = apiService.getUserProfile()
        Log.d("DASHBOARD_DEBUG", "Raw Profile DTO: ${dto.toDomain()}")
        dto.toDomain()
    }

    override suspend fun getHomeDashboard(): Result<DashboardSummary> = safeApi {
        val dto = apiService.getHomeDashboard()
        // LOG RAW NETWORK DATA HERE
        Log.d("DASHBOARD_DEBUG", "Raw Dashboard DTO: $dto")
        Log.d("DASHBOARD_DEBUG", "Today Focus: ${dto.todayFocus}")
        Log.d("DASHBOARD_DEBUG", "Today Tasks: ${dto.todayTasks}")
        dto.toDomain()
    }
}