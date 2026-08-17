package com.iti.mongez.domain.organization.model

data class Team(
    val id: String,
    val name: String,
    val photoUrl: String?,
    val memberCount: Int,
    val progress: Int,
    val events: List<String> = emptyList() // Depending on what events contains
)

data class TrendingTeam(
    val teamId: String,
    val name: String,
    val imageUrl: String?,
    val organizationName: String?,
    val status: String
)

data class PendingInvitation(
    val id: String,
    val name: String,
    val organizationName: String,
    val imageUrl: String?,
    val appliedDate: String
)

data class DiscoverTeams(
    val pendingInvitations: List<PendingInvitation>,
    val trendingTeams: List<TrendingTeam>
)

data class JoinTeamData(
    val orgId: String?,
    val orgName: String?,
    val teamId: String?,
    val teamName: String?,
    val status: String?,
    val message: String?
)

data class JoinTeamResponse(
    val success: Boolean,
    val data: JoinTeamData?,
    val message: String?,
    val error: String?,
    val details: String?
)

data class TeamEvent(
    val id: String,
    val courseName: String,
    val eventType: String,
    val dueText: String,
    val eventDate: String
)
