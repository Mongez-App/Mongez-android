package com.iti.mongez.domain.auth.repository

import com.iti.mongez.domain.auth.model.User
import com.iti.mongez.domain.core.Result
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    
    suspend fun register(firstName: String, email: String, password: String): Result<User>
    
    suspend fun loginWithGoogle(idToken: String): Result<User>
    
    suspend fun hasToken(): Boolean

    suspend fun logout()

    suspend fun getCurrentUser(): User?

    fun getCurrentUserFlow(): Flow<User?>
}
