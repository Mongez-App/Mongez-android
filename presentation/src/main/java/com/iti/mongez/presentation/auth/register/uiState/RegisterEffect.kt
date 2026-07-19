package com.iti.mongez.presentation.auth.register.uiState

import com.iti.mongez.domain.auth.model.User

sealed interface RegisterEffect {
    data class NavigateToHome(val user: User) : RegisterEffect
    object NavigateToLogin : RegisterEffect
    object LaunchGoogleSignUp : RegisterEffect
    data class ShowError(val message: String) : RegisterEffect
}
