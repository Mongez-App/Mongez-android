package com.iti.mongez.data.mapper

import com.iti.mongez.data.dtos.organizationdtos.DiscoverTeamsResponseDto
import com.iti.mongez.data.dtos.organizationdtos.TeamCourseDto
import com.iti.mongez.data.dtos.organizationdtos.TeamDto
import com.iti.mongez.data.dtos.organizationdtos.TeamEventDto
import com.iti.mongez.data.dtos.organizationdtos.TrendingTeamDto
import com.iti.mongez.data.dtos.organizationdtos.PendingInvitationDto
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.organization.model.DiscoverTeams
import com.iti.mongez.domain.organization.model.Team
import com.iti.mongez.domain.organization.model.TeamEvent
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

fun TeamCourseDto.toDomain(): Course {
    return Course(
        id = courseId.orEmpty(),
        name = name.orEmpty(),
        courseCode = courseCode.orEmpty(),
        imageUrl = courseImageUrl,
        startDate = startDate.orEmpty(),
        examDate = endDate.orEmpty(),
        hasMaterials = false,
        completionPercentage = completionPercentage ?: 0f,
        isHidden = false,
        courseType = "STANDARD",
        materialUrl = null
    )
}

fun TeamEventDto.toDomain(): TeamEvent {
    return TeamEvent(
        id = eventId.orEmpty(),
        courseName = courseName.orEmpty(),
        eventType = eventType.orEmpty(),
        dueText = dueText.orEmpty(),
        eventDate = eventDate.orEmpty()
    )
}
