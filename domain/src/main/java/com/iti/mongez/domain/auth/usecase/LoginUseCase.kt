package com.iti.mongez.domain.auth.usecase

import com.iti.mongez.domain.auth.model.User
import com.iti.mongez.domain.auth.repository.AuthRepository
import com.iti.mongez.domain.core.Result
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.isBlank()) {
            return Result.Failure(IllegalArgumentException("Email and password cannot be empty"))
        }
        return authRepository.login(email, password)
    }
}
