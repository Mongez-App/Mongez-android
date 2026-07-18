package com.iti.mongez.domain.dashboard.repository

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.dashboard.model.DashboardSummary
import com.iti.mongez.domain.dashboard.model.UserProfile

interface DashboardRepository {
    suspend fun getUserProfile(): Result<UserProfile>
    suspend fun getHomeDashboard(): Result<DashboardSummary>
}