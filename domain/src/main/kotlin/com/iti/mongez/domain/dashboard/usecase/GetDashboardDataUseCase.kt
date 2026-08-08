package com.iti.mongez.domain.dashboard.usecase

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.core.exceptions.AppException
import com.iti.mongez.domain.dashboard.model.DashboardAggregatedData
import com.iti.mongez.domain.dashboard.repository.DashboardRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class GetDashboardDataUseCase @Inject constructor(
    private val repository: DashboardRepository
) {
    suspend operator fun invoke(): Result<DashboardAggregatedData> = coroutineScope {
        // Fire both network requests concurrently to optimize performance
        val profileDeferred = async { repository.getUserProfile() }
        val dashboardDeferred = async { repository.getHomeDashboard() }

        // Await the results from both operations
        val profileResult = profileDeferred.await()
        val dashboardResult = dashboardDeferred.await()

        // If both calls succeed, aggregate and return the data combined
        if (profileResult is Result.Success && dashboardResult is Result.Success) {
            Result.Success(
                DashboardAggregatedData(
                    userName = profileResult.data.name,
                    avatarUrl = profileResult.data.avatarUrl,
                    streak = profileResult.data.currentStreakDays,
                    summary = dashboardResult.data
                )
            )
        } else {
            // Extract the exception from whichever request failed first
            val error = (profileResult as? Result.Failure)?.exception
                ?: (dashboardResult as? Result.Failure)?.exception
                ?: AppException.UnknownException("Unknown Dashboard Error")

            Result.Failure(error)
        }
    }
}