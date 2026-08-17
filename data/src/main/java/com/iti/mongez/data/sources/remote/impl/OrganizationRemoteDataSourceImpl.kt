package com.iti.mongez.data.sources.remote.impl

import com.iti.mongez.data.dtos.organizationdtos.DiscoverTeamsResponseDto
import com.iti.mongez.data.dtos.organizationdtos.TeamDto
import com.iti.mongez.data.sources.remote.interfaces.OrganizationRemoteDataSource
import com.iti.mongez.data.sources.remote.services.OrganizationApiService
import javax.inject.Inject

class OrganizationRemoteDataSourceImpl @Inject constructor(
    private val apiService: OrganizationApiService
) : OrganizationRemoteDataSource {

    override suspend fun getTeams(): List<TeamDto> {
        return apiService.getTeams()
    }

    override suspend fun getDiscoverTeams(): com.iti.mongez.data.dtos.organizationdtos.DiscoverTeamsResponseDto {
        return apiService.getDiscoverTeams()
    }

    override suspend fun joinTeam(request: com.iti.mongez.data.dtos.organizationdtos.JoinTeamRequestDto): com.iti.mongez.data.dtos.organizationdtos.JoinTeamResponseDto {
        return apiService.joinTeam(request)
    }
}
