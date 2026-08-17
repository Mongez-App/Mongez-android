package com.iti.mongez.data.sources.remote.services

import retrofit2.http.GET
import retrofit2.http.Path

interface OrganizationApiService {
    @GET("teams")
    suspend fun getTeams(): List<com.iti.mongez.data.dtos.organizationdtos.TeamDto>

    @GET("teams/discover")
    suspend fun getDiscoverTeams(): com.iti.mongez.data.dtos.organizationdtos.DiscoverTeamsResponseDto

    @retrofit2.http.POST("teams/join")
    suspend fun joinTeam(@retrofit2.http.Body request: com.iti.mongez.data.dtos.organizationdtos.JoinTeamRequestDto): com.iti.mongez.data.dtos.organizationdtos.JoinTeamResponseDto

    @GET("teams/{teamId}/courses")
    suspend fun getTeamCourses(@Path("teamId") teamId: String): List<com.iti.mongez.data.dtos.organizationdtos.TeamCourseDto>

    @GET("teams/team/{teamId}/events")
    suspend fun getTeamEvents(@Path("teamId") teamId: String): List<com.iti.mongez.data.dtos.organizationdtos.TeamEventDto>
}
