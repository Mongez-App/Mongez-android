package com.iti.mongez.data.dtos.organizationdtos

import com.google.gson.annotations.SerializedName

data class EventDto(
    @SerializedName("event_type") val eventType: String
)

data class TeamDto(
    @SerializedName("team_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val photoUrl: String?,
    @SerializedName("organization_name") val organizationName: String?,
    @SerializedName("completion_percentage") val progress: Float?,
    @SerializedName("events") val events: List<EventDto>?
)

data class TrendingTeamDto(
    @SerializedName("team_id") val teamId: String,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("organization_name") val organizationName: String?,
    @SerializedName("status") val status: String
)

data class PendingInvitationDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("organizationName") val organizationName: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("appliedDate") val appliedDate: String?
)

data class DiscoverTeamsResponseDto(
    @SerializedName("pending_requests") val pendingInvitations: List<PendingInvitationDto>?,
    @SerializedName("trending_teams") val trendingTeams: List<TrendingTeamDto>?
)

data class JoinTeamRequestDto(
    @SerializedName("inviteCode") val inviteCode: String
)

data class JoinTeamDataDto(
    @SerializedName("org_id") val orgId: String?,
    @SerializedName("org_name") val orgName: String?,
    @SerializedName("team_id") val teamId: String?,
    @SerializedName("team_name") val teamName: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("message") val message: String?
)

data class JoinTeamResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data") val data: JoinTeamDataDto?,
    @SerializedName("message") val message: String?,
    @SerializedName("error") val error: String?,
    @SerializedName("details") val details: String?
)

data class TeamCourseDto(
    @SerializedName("course_id") val courseId: String?,
    @SerializedName("team_id") val teamId: String?,
    @SerializedName("user_id") val userId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("course_code") val courseCode: String?,
    @SerializedName("start_date") val startDate: String?,
    @SerializedName("end_date") val endDate: String?,
    @SerializedName("course_image_url") val courseImageUrl: String?,
    @SerializedName("completion_percentage") val completionPercentage: Float?
)

data class TeamEventDto(
    @SerializedName("event_id") val eventId: String?,
    @SerializedName("course_name") val courseName: String?,
    @SerializedName("event_type") val eventType: String?,
    @SerializedName("due_text") val dueText: String?,
    @SerializedName("event_date") val eventDate: String?
)
