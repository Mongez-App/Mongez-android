package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.DashboardResponseDto
import com.iti.mongez.data.dtos.ProfileResponseDto
import retrofit2.http.GET

interface ApiService {
    @GET("users/me/profile")
    suspend fun getUserProfile(): ProfileResponseDto

    @GET("home/dashboard")
    suspend fun getHomeDashboard(): DashboardResponseDto
}