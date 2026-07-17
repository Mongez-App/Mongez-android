package com.iti.mongez.presentation.auth.login

import com.iti.mongez.domain.auth.model.User

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false
)

sealed interface LoginIntent {
    data class OnEmailChanged(val email: String) : LoginIntent
    data class OnPasswordChanged(val password: String) : LoginIntent
    object OnLoginClicked : LoginIntent
    object OnGuestClicked : LoginIntent
    object OnGoogleSignInClicked : LoginIntent
    object OnSignUpClicked : LoginIntent
    object OnForgotPasswordClicked : LoginIntent
}

sealed interface LoginEffect {
    data class NavigateToHome(val user: User? = null) : LoginEffect
    object NavigateToSignUp : LoginEffect
    object NavigateToForgotPassword : LoginEffect
    data class ShowError(val message: String) : LoginEffect
}
