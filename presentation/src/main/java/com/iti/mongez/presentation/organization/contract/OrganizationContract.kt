package com.iti.mongez.presentation.organization.contract

import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType

sealed interface OrganizationIntent {
    object LoadData : OrganizationIntent
    data class Refresh(val showLoading: Boolean = false) : OrganizationIntent
}

sealed interface OrganizationEffect {
    data class ShowSnackbar(val message: String, val type: AppSnackbarType = AppSnackbarType.Info) : OrganizationEffect
    data class NavigateToTeamCourses(val teamId: String) : OrganizationEffect
}
