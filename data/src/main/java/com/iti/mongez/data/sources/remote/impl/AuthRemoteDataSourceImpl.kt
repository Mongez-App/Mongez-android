package com.iti.mongez.data.sources.remote.impl

import com.iti.mongez.data.dtos.AuthResponseDto
import com.iti.mongez.data.dtos.HandshakeRequestDto
import com.iti.mongez.data.sources.remote.interfaces.AuthRemoteDataSource
import com.iti.mongez.data.sources.remote.services.AuthApiService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(
    private val authApiService: AuthApiService
) : AuthRemoteDataSource {
    override suspend fun getUserProfile(): AuthResponseDto {
        return authApiService.getUserProfile()
    }

    override suspend fun handshake(request: HandshakeRequestDto): AuthResponseDto {
        return authApiService.handshake(request)
    }
}
