package com.iti.mongez.data.sources.remote.interfaces

import com.iti.mongez.data.dtos.organizationdtos.DiscoverTeamsResponseDto
import com.iti.mongez.data.dtos.organizationdtos.TeamDto

interface OrganizationRemoteDataSource {
    suspend fun getTeams(): List<TeamDto>
    suspend fun getDiscoverTeams(): DiscoverTeamsResponseDto
    suspend fun joinTeam(request: com.iti.mongez.data.dtos.organizationdtos.JoinTeamRequestDto): com.iti.mongez.data.dtos.organizationdtos.JoinTeamResponseDto
}
