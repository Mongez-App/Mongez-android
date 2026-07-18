package com.iti.mongez.domain.auth.repository

import com.iti.mongez.domain.auth.model.User
import com.iti.mongez.domain.core.Result

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<User>
    
    suspend fun register(firstName: String, email: String, password: String): Result<User>
    
    suspend fun loginWithGoogle(idToken: String): Result<User>

    suspend fun isLoggedIn(): Result<Boolean>
}
