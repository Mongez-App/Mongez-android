package com.iti.mongez.domain.auth.usecase

import com.iti.mongez.domain.auth.model.User
import com.iti.mongez.domain.auth.repository.AuthRepository
import com.iti.mongez.domain.core.Result
import javax.inject.Inject

class LoginWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<User> {
        if (idToken.isBlank()) {
            return Result.Failure(IllegalArgumentException("Google ID token cannot be empty"))
        }
        return authRepository.loginWithGoogle(idToken)
    }
}
