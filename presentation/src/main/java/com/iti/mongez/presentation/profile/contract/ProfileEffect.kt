package com.iti.mongez.presentation.profile.contract

import com.iti.mongez.presentation.utils.UiText

sealed interface ProfileEffect {
    object NavigateToLogin : ProfileEffect
    data class ShowError(val message: UiText) : ProfileEffect
}
