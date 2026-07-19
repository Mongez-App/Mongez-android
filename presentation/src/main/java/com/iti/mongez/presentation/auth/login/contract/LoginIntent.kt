package com.iti.mongez.presentation.auth.login.contract

sealed interface LoginIntent {
    data class OnEmailChanged(val email: String) : LoginIntent
    data class OnPasswordChanged(val password: String) : LoginIntent
    data class OnGoogleIdTokenReceived(val idToken: String) : LoginIntent
    object OnLoginClicked : LoginIntent
    object OnGuestClicked : LoginIntent
    object OnGoogleSignInClicked : LoginIntent
    object OnSignUpClicked : LoginIntent
    object OnForgotPasswordClicked : LoginIntent
}
