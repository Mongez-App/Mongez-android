package com.iti.mongez.data.mapper

import com.iti.mongez.data.dtos.organizationdtos.DiscoverTeamsResponseDto
import com.iti.mongez.data.dtos.organizationdtos.TeamDto
import com.iti.mongez.data.dtos.organizationdtos.TrendingTeamDto
import com.iti.mongez.data.dtos.organizationdtos.PendingInvitationDto
import com.iti.mongez.domain.organization.model.DiscoverTeams
import com.iti.mongez.domain.organization.model.Team
import com.iti.mongez.domain.organization.model.TrendingTeam

fun TeamDto.toDomain(): Team {
    return Team(
        id = id,
        name = name,
        photoUrl = photoUrl,
        memberCount = 0, // Not provided by the current JSON
        progress = progress?.toInt() ?: 0,
        events = events?.map { it.eventType } ?: emptyList()
    )
}

fun TrendingTeamDto.toDomain(): TrendingTeam {
    return TrendingTeam(
        teamId = teamId,
        name = name,
        imageUrl = imageUrl,
        organizationName = organizationName,
        status = status
    )
}

fun PendingInvitationDto.toDomain(): com.iti.mongez.domain.organization.model.PendingInvitation {
    return com.iti.mongez.domain.organization.model.PendingInvitation(
        id = id ?: "",
        name = name ?: "Unknown Team",
        organizationName = organizationName ?: "Unknown Organization",
        imageUrl = imageUrl,
        appliedDate = appliedDate ?: ""
    )
}

fun DiscoverTeamsResponseDto.toDomain(): DiscoverTeams {
    return DiscoverTeams(
        pendingInvitations = pendingInvitations?.map { it.toDomain() } ?: emptyList(),
        trendingTeams = trendingTeams?.map { it.toDomain() } ?: emptyList()
    )
}

fun com.iti.mongez.data.dtos.organizationdtos.JoinTeamDataDto.toDomain(): com.iti.mongez.domain.organization.model.JoinTeamData {
    return com.iti.mongez.domain.organization.model.JoinTeamData(
        orgId = orgId,
        orgName = orgName,
        teamId = teamId,
        teamName = teamName,
        status = status,
        message = message
    )
}

fun com.iti.mongez.data.dtos.organizationdtos.JoinTeamResponseDto.toDomain(): com.iti.mongez.domain.organization.model.JoinTeamResponse {
    return com.iti.mongez.domain.organization.model.JoinTeamResponse(
        success = success,
        data = data?.toDomain(),
        message = message,
        error = error,
        details = details
    )
}
