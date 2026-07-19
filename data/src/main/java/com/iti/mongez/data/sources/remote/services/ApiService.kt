package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.DashboardResponseDto
import com.iti.mongez.data.dtos.ProfileResponseDto
import com.iti.mongez.data.dtos.RegisterRequestDto
import com.iti.mongez.data.dtos.LoginRequestDto
import com.iti.mongez.data.dtos.AuthResponseDto
import com.iti.mongez.data.dtos.CalendarStatusDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("users/me/profile")
    suspend fun getUserProfile(): ProfileResponseDto

    @GET("home/dashboard")
    suspend fun getHomeDashboard(): DashboardResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): AuthResponseDto

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): AuthResponseDto

    @POST("auth/calendar/connect")
    suspend fun connectCalendar(): Unit

    @GET("auth/calendar/status")
    suspend fun getCalendarStatus(): CalendarStatusDto

    @DELETE("auth/calendar/disconnect")
    suspend fun disconnectCalendar(): Unit
}