package com.iti.mongez.data.sources.remote.interfaces

import com.iti.mongez.data.dtos.dashboarddtos.DashboardResponseDto

interface DashboardRemoteDataSource {
    suspend fun getHomeDashboard(): DashboardResponseDto
}
