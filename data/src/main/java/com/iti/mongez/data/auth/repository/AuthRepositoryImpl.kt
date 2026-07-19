package com.iti.mongez.data.repositories.auth.repository

import com.iti.mongez.data.dtos.LoginRequestDto
import com.iti.mongez.data.dtos.RegisterRequestDto
import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.data.sources.local.TokenManager
import com.iti.mongez.data.sources.remote.services.ApiService
import com.iti.mongez.data.network.safeApi
import com.iti.mongez.data.sources.remote.FirebaseAuthDataSource
import com.iti.mongez.domain.auth.model.User
import com.iti.mongez.domain.auth.repository.AuthRepository
import com.iti.mongez.domain.core.Result
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuthDataSource: FirebaseAuthDataSource,
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return safeApi {
            val firebaseToken = firebaseAuthDataSource.signInWithEmail(email, password)
            val response = apiService.login(LoginRequestDto(userToken = firebaseToken))
            tokenManager.saveToken(firebaseToken)
            response.toDomain()
        }
    }

    override suspend fun register(firstName: String, email: String, password: String): Result<User> {
        return safeApi {
            val firebaseToken = firebaseAuthDataSource.createUserWithEmail(email, password)
            val response = apiService.register(
                RegisterRequestDto(
                    userToken = firebaseToken,
                    name = firstName
                )
            )
            tokenManager.saveToken(firebaseToken)
            response.toDomain()
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        return safeApi {
            val firebaseToken = firebaseAuthDataSource.signInWithGoogleCredential(idToken)
            val response = apiService.login(LoginRequestDto(userToken = firebaseToken))
            tokenManager.saveToken(firebaseToken)
            response.toDomain()
        }
    }

    override suspend fun hasToken(): Boolean {
        val token = tokenManager.tokenFlow.firstOrNull()
        return !token.isNullOrEmpty()
    }
}
