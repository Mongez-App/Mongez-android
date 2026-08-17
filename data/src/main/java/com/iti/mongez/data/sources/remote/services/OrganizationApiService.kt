package com.iti.mongez.data.sources.remote.services

import retrofit2.http.GET

interface OrganizationApiService {
    @GET("teams")
    suspend fun getTeams(): List<com.iti.mongez.data.dtos.organizationdtos.TeamDto>

    @GET("teams/discover")
    suspend fun getDiscoverTeams(): com.iti.mongez.data.dtos.organizationdtos.DiscoverTeamsResponseDto

    @retrofit2.http.POST("teams/join")
    suspend fun joinTeam(@retrofit2.http.Body request: com.iti.mongez.data.dtos.organizationdtos.JoinTeamRequestDto): com.iti.mongez.data.dtos.organizationdtos.JoinTeamResponseDto
}
