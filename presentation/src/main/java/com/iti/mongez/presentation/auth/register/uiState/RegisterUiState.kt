package com.iti.mongez.presentation.auth.register.uiState

data class RegisterUiState(
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
