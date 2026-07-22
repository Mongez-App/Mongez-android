package com.iti.mongez.data.repositories.auth.repository

import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.data.mapper.toEntity
import com.iti.mongez.data.dtos.HandshakeRequestDto
import com.iti.mongez.data.local.dao.UserDao
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
    private val tokenManager: TokenManager,
    private val userDao: UserDao
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return safeApi {
            val firebaseToken = firebaseAuthDataSource.signInWithEmail(email, password)
            tokenManager.saveToken(firebaseToken)
            println("Firebase Token : $firebaseToken")
            val response = apiService.handshake(HandshakeRequestDto(isGuest = false))
            val user = response.toDomain()
            userDao.insertUser(user.toEntity())
            user
        }
    }

    override suspend fun register(firstName: String, email: String, password: String): Result<User> {
        return safeApi {
            val firebaseToken = firebaseAuthDataSource.createUserWithEmail(email, password)
            tokenManager.saveToken(firebaseToken)
            println("Firebase Token : $firebaseToken")
            val response = apiService.handshake(HandshakeRequestDto(isGuest = false))
            val user = response.toDomain()
            userDao.insertUser(user.toEntity())
            user
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        return safeApi {
            val firebaseToken = firebaseAuthDataSource.signInWithGoogleCredential(idToken)
            tokenManager.saveToken(firebaseToken)
            val response = apiService.handshake(HandshakeRequestDto(isGuest = false))
            val user = response.toDomain()
            userDao.insertUser(user.toEntity())
            user
        }
    }

    override suspend fun hasToken(): Boolean {
        val token = tokenManager.tokenFlow.firstOrNull()
        return !token.isNullOrEmpty()
    }

    override suspend fun logout() {
        tokenManager.clearToken()
    }
}
