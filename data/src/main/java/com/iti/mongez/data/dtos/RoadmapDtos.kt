package com.iti.mongez.data.dtos

import com.google.gson.annotations.SerializedName
import com.iti.mongez.domain.roadmap.model.*

data class WeeklyRoadmapDto(
    @SerializedName("roadmap_start_date") val roadmapStartDate: String,
    @SerializedName("weeks") val weeks: List<RoadmapWeekDto>
)

data class RoadmapWeekDto(
    @SerializedName("week_number") val weekNumber: Int,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("days") val days: List<RoadmapDayDto>
)

data class RoadmapDayDto(
    @SerializedName("date") val date: String,
    @SerializedName("day_name") val dayName: String,
    @SerializedName("study_blocks") val studyBlocks: List<StudyBlockDto>
)

data class StudyBlockDto(
    @SerializedName("block_id") val blockId: String,
    @SerializedName("course_name") val courseName: String,
    @SerializedName("topic") val topic: String,
    @SerializedName("duration_minutes") val durationMinutes: Int,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("event") val event: RoadmapEventDto? = null
)

data class RoadmapEventDto(
    @SerializedName("title") val title: String,
    @SerializedName("type") val type: String
)

fun WeeklyRoadmapDto.toDomain() = WeeklyRoadmap(
    roadmapStartDate = roadmapStartDate,
    weeks = weeks.map { it.toDomain() }
)

fun RoadmapWeekDto.toDomain() = RoadmapWeek(
    weekNumber = weekNumber,
    startDate = startDate,
    endDate = endDate,
    days = days.map { it.toDomain() }
)

fun RoadmapDayDto.toDomain() = RoadmapDay(
    date = date,
    dayName = dayName,
    studyBlocks = studyBlocks.map { it.toDomain() }
)

fun StudyBlockDto.toDomain() = StudyBlock(
    id = blockId,
    courseName = courseName,
    topic = topic,
    durationMinutes = durationMinutes,
    isCompleted = isCompleted,
    event = event?.toDomain()
)

fun RoadmapEventDto.toDomain() = RoadmapEvent(
    title = title,
    type = type
)
