package com.iti.mongez.data.sources.remote.services

import com.iti.mongez.data.dtos.AuthResponseDto
import com.iti.mongez.data.dtos.HandshakeRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    @GET("auth/me")
    suspend fun getUserProfile(): AuthResponseDto

    @POST("auth/handshake")
    suspend fun handshake(@Body request: HandshakeRequestDto): AuthResponseDto
}
