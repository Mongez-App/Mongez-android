package com.iti.mongez.presentation.profile.contract

sealed interface ProfileEffect {
    object NavigateToLogin : ProfileEffect
    data object NavigateToPreferences : ProfileEffect
    data class ShowError(val message: String) : ProfileEffect
}
