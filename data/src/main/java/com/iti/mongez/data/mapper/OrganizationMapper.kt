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
        memberCount = memberCount,
        progress = progress,
        events = events
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
        id = id,
        name = name,
        organizationName = organizationName,
        imageUrl = imageUrl,
        appliedDate = appliedDate
    )
}

fun DiscoverTeamsResponseDto.toDomain(): DiscoverTeams {
    return DiscoverTeams(
        pendingInvitations = pendingInvitations.map { it.toDomain() },
        trendingTeams = trendingTeams.map { it.toDomain() }
    )
}
