package com.iti.mongez.presentation.auth.register

import com.iti.mongez.domain.auth.model.User

data class RegisterState(
    val firstName: String = "",
    val firstNameError: String? = null,
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false
)

sealed interface RegisterIntent {
    data class OnFirstNameChanged(val name: String) : RegisterIntent
    data class OnEmailChanged(val email: String) : RegisterIntent
    data class OnPasswordChanged(val password: String) : RegisterIntent
    data class OnConfirmPasswordChanged(val password: String) : RegisterIntent
    object OnRegisterClicked : RegisterIntent
    object OnGoogleSignUpClicked : RegisterIntent
    object OnLoginClicked : RegisterIntent
}

sealed interface RegisterEffect {
    data class NavigateToHome(val user: User) : RegisterEffect
    object NavigateToLogin : RegisterEffect
    data class ShowError(val message: String) : RegisterEffect
}
