package com.iti.mongez.data.auth.repository

import com.iti.mongez.data.auth.source.AuthLocalDataSource
import com.iti.mongez.domain.auth.model.User
import com.iti.mongez.domain.auth.repository.AuthRepository
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.core.exceptions.AuthException
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockAuthRepository @Inject constructor(
    private val localDataSource: AuthLocalDataSource
) : AuthRepository {
    override suspend fun login(email: String, password: String): Result<User> {
        delay(1500) // Simulate network delay
        if (email == "test@test.com" && password == "password") {
            localDataSource.setLoggedIn(true)
            return Result.Success(User(id = "1", email = email, firstName = "Test", token = "mock_token"))
        } else if (password != "password") {
            return Result.Failure(AuthException.InvalidCredentials())
        }
        localDataSource.setLoggedIn(true)
        return Result.Success(User(id = "1", email = email, firstName = "Test", token = "mock_token"))
    }

    override suspend fun register(firstName: String, email: String, password: String): Result<User> {
        delay(1500) // Simulate network delay
        localDataSource.setLoggedIn(true)
        return Result.Success(User(id = "2", email = email, firstName = firstName, token = "mock_token"))
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        delay(1500) // Simulate network delay
        localDataSource.setLoggedIn(true)
        return Result.Success(User(id = "3", email = "google@user.com", firstName = "Google", token = "mock_token"))
    }

    override suspend fun isLoggedIn(): Result<Boolean> {
        return Result.Success(localDataSource.isLoggedIn())
    }
}
