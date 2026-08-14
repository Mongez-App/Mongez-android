package com.iti.mongez.data.sources.remote.impl

import com.iti.mongez.data.dtos.dashboarddtos.DashboardResponseDto
import com.iti.mongez.data.sources.remote.interfaces.DashboardRemoteDataSource
import com.iti.mongez.data.sources.remote.services.ApiService
import javax.inject.Inject

class DashboardRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : DashboardRemoteDataSource {
    override suspend fun getHomeDashboard(): DashboardResponseDto {
        return apiService.getHomeDashboard()
    }
}
