package com.iti.mongez.data.sources.remote.interfaces

import com.iti.mongez.data.dtos.AuthResponseDto
import com.iti.mongez.data.dtos.HandshakeRequestDto

interface AuthRemoteDataSource {
    suspend fun getUserProfile(): AuthResponseDto
    suspend fun handshake(request: HandshakeRequestDto): AuthResponseDto
}
