package com.iti.mongez.data.sources.remote.impl

import com.iti.mongez.data.dtos.organizationdtos.DiscoverTeamsResponseDto
import com.iti.mongez.data.dtos.organizationdtos.MyTeamsResponseDto
import com.iti.mongez.data.dtos.organizationdtos.TrendingTeamDto
import com.iti.mongez.data.sources.remote.interfaces.OrganizationRemoteDataSource
import com.iti.mongez.data.sources.remote.services.OrganizationApiService
import javax.inject.Inject

class OrganizationRemoteDataSourceImpl @Inject constructor(
    private val apiService: OrganizationApiService
) : OrganizationRemoteDataSource {

    override suspend fun getTeams(): MyTeamsResponseDto {
        return apiService.getTeams()
    }

    override suspend fun getDiscoverTeams(): DiscoverTeamsResponseDto {
        // Return dummy data as requested
        return DiscoverTeamsResponseDto(
            pendingInvitations = listOf(
                com.iti.mongez.data.dtos.organizationdtos.PendingInvitationDto(
                    id = "1",
                    name = "Team Name",
                    organizationName = "Organization Name",
                    imageUrl = null,
                    appliedDate = "Applied May 25"
                )
            ),
            trendingTeams = listOf(
                TrendingTeamDto(
                    teamId = "2",
                    name = "Team Name",
                    imageUrl = null,
                    organizationName = "Organization Name",
                    status = "NOT_A_MEMBER"
                )
            )
        )
    }
}
