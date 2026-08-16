package com.iti.mongez.data.dtos.organizationdtos

import com.google.gson.annotations.SerializedName

data class TeamDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("photoUrl") val photoUrl: String?,
    @SerializedName("memberCount") val memberCount: Int,
    @SerializedName("progress") val progress: Int,
    @SerializedName("events") val events: List<String>
)

data class MyTeamsResponseDto(
    @SerializedName("teams") val teams: List<TeamDto>,
    @SerializedName("total") val total: Int
)

data class TrendingTeamDto(
    @SerializedName("team_id") val teamId: String,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("organization_name") val organizationName: String?,
    @SerializedName("status") val status: String
)

data class PendingInvitationDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("organizationName") val organizationName: String,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("appliedDate") val appliedDate: String
)

data class DiscoverTeamsResponseDto(
    @SerializedName("pendingInvitations") val pendingInvitations: List<PendingInvitationDto>,
    @SerializedName("trendingTeams") val trendingTeams: List<TrendingTeamDto>
)
