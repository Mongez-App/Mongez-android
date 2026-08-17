package com.iti.mongez.domain.organization.repository

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.organization.model.DiscoverTeams
import com.iti.mongez.domain.organization.model.Team

interface OrganizationRepository {
    suspend fun getMyTeams(): Result<List<Team>>
    suspend fun getDiscoverTeams(): Result<DiscoverTeams>
    suspend fun joinTeam(inviteCode: String): Result<com.iti.mongez.domain.organization.model.JoinTeamResponse>
}
