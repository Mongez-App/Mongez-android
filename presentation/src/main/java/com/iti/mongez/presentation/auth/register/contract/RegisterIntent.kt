package com.iti.mongez.presentation.auth.register.contract

sealed interface RegisterIntent {
    data class OnFirstNameChanged(val name: String) : RegisterIntent
    data class OnEmailChanged(val email: String) : RegisterIntent
    data class OnPasswordChanged(val password: String) : RegisterIntent
    data class OnConfirmPasswordChanged(val password: String) : RegisterIntent
    data class OnGoogleIdTokenReceived(val idToken: String) : RegisterIntent
    object OnRegisterClicked : RegisterIntent
    object OnGoogleSignUpClicked : RegisterIntent
    object OnLoginClicked : RegisterIntent
    object ClearFields : RegisterIntent
}
