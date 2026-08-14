package com.iti.mongez.data.repositories.dashboard

import android.util.Log
import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.interfaces.DashboardRemoteDataSource
import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.data.sources.remote.interfaces.UserRemoteDataSource
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.dashboard.model.DashboardSummary
import com.iti.mongez.domain.dashboard.model.UserProfile
import com.iti.mongez.domain.dashboard.repository.DashboardRepository
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val dashboardRemoteDataSource: DashboardRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : DashboardRepository {

    override suspend fun getUserProfile(): Result<UserProfile> = safeApi {
        val dto = userRemoteDataSource.getFullUserProfile()
        Log.d("DASHBOARD_DEBUG", "Raw Profile DTO: $dto")
        UserProfile(
            name = dto.name?: "No name",
            avatarUrl = dto.avatarUrl,
            currentStreakDays = dto.stats?.currentStreakDays ?: 0
        )
    }

    override suspend fun getHomeDashboard(): Result<DashboardSummary> = safeApi {
        val dto = dashboardRemoteDataSource.getHomeDashboard()
        // LOG RAW NETWORK DATA HERE
        Log.d("DASHBOARD_DEBUG", "Raw Dashboard DTO: $dto")
        Log.d("DASHBOARD_DEBUG", "Today Focus: ${dto.todayFocus}")
        Log.d("DASHBOARD_DEBUG", "Today Tasks: ${dto.todayTasks}")
        dto.toDomain()
    }
}