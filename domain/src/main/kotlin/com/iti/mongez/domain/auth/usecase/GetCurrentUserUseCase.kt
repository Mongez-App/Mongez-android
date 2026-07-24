package com.iti.mongez.domain.auth.usecase

import com.iti.mongez.domain.auth.model.User
import com.iti.mongez.domain.auth.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): User? = authRepository.getCurrentUser()
}
