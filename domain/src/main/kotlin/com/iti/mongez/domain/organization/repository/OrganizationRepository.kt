package com.iti.mongez.domain.organization.repository

import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.organization.model.DiscoverTeams
import com.iti.mongez.domain.organization.model.Team
import com.iti.mongez.domain.organization.model.TeamEvent

interface OrganizationRepository {
    suspend fun getMyTeams(): Result<List<Team>>
    suspend fun getDiscoverTeams(): Result<DiscoverTeams>
    suspend fun joinTeam(inviteCode: String): Result<com.iti.mongez.domain.organization.model.JoinTeamResponse>
    suspend fun getTeamCourses(teamId: String): Result<List<Course>>
    suspend fun getTeamEvents(teamId: String): Result<List<TeamEvent>>
}
