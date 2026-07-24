package com.iti.mongez.presentation.auth.login.uiState

import com.iti.mongez.domain.auth.model.User

sealed interface LoginEffect {
    data class NavigateToHome(val user: User? = null, val isPreferencesSet: Boolean = false) : LoginEffect
    object NavigateToSignUp : LoginEffect
    object NavigateToForgotPassword : LoginEffect
    object LaunchGoogleSignIn : LoginEffect
    data class ShowError(val message: String) : LoginEffect
}
