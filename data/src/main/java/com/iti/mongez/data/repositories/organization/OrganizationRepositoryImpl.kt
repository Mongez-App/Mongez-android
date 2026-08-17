package com.iti.mongez.data.repositories.organization

import com.iti.mongez.data.mapper.toDomain
import com.iti.mongez.data.sources.remote.interfaces.OrganizationRemoteDataSource
import com.iti.mongez.data.network.safeApi
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.organization.model.DiscoverTeams
import com.iti.mongez.domain.organization.model.Team
import com.iti.mongez.domain.organization.model.TeamEvent
import com.iti.mongez.domain.organization.repository.OrganizationRepository
import javax.inject.Inject

class OrganizationRepositoryImpl @Inject constructor(
    private val remoteDataSource: OrganizationRemoteDataSource
) : OrganizationRepository {

    override suspend fun getMyTeams(): Result<List<Team>> {
        return safeApi {
            val response = remoteDataSource.getTeams()
            response.map { it.toDomain() }
        }
    }

    override suspend fun getDiscoverTeams(): Result<DiscoverTeams> {
        return safeApi {
            val response = remoteDataSource.getDiscoverTeams()
            response.toDomain()
        }
    }

    override suspend fun joinTeam(inviteCode: String): Result<com.iti.mongez.domain.organization.model.JoinTeamResponse> {
        return safeApi {
            val request = com.iti.mongez.data.dtos.organizationdtos.JoinTeamRequestDto(inviteCode)
            val response = remoteDataSource.joinTeam(request)
            response.toDomain()
        }
    }

    override suspend fun getTeamCourses(teamId: String): Result<List<Course>> {
        return safeApi {
            val response = remoteDataSource.getTeamCourses(teamId)
            response.map { it.toDomain() }
        }
    }

    override suspend fun getTeamEvents(teamId: String): Result<List<TeamEvent>> {
        return safeApi {
            val response = remoteDataSource.getTeamEvents(teamId)
            response.map { it.toDomain() }
        }
    }
}
