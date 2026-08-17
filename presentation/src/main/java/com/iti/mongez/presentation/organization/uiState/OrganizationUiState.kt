package com.iti.mongez.presentation.organization.uiState

import com.iti.mongez.domain.organization.model.DiscoverTeams
import com.iti.mongez.domain.organization.model.Team

sealed interface OrganizationUiState {
    object Loading : OrganizationUiState
    
    data class Success(
        val myTeams: List<Team>,
        val discoverTeams: DiscoverTeams
    ) : OrganizationUiState
    
    data class Error(val message: String) : OrganizationUiState
}

sealed interface JoinTeamState {
    object Idle : JoinTeamState
    object Loading : JoinTeamState
    data class Success(val message: String) : JoinTeamState
    data class Error(val message: String) : JoinTeamState
}
