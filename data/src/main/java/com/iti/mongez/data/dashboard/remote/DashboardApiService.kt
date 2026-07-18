package com.iti.mongez.data.dashboard.remote

import retrofit2.http.GET

interface DashboardApiService {
    @GET("users/me/profile")
    suspend fun getUserProfile(): ProfileResponseDto

    @GET("home/dashboard")
    suspend fun getHomeDashboard(): DashboardResponseDto
}