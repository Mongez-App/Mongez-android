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
    @SerializedName("study_blocks") val studyBlocks: List<StudyBlockDto>
)

data class StudyBlockDto(
    @SerializedName("block_id") val blockId: String,
    @SerializedName("course_id") val courseId: String,
    @SerializedName("course_name") val courseName: String,
    @SerializedName("tasks") val tasks: List<RoadmapTaskDto>,
    @SerializedName("events") val events: List<RoadmapEventDto>,
    @SerializedName("is_completed") val isCompleted: Boolean
)

data class RoadmapTaskDto(
    @SerializedName("topic") val topic: String,
    @SerializedName("duration_minutes") val durationMinutes: Int,
    @SerializedName("task_date") val taskDate: String
)

data class RoadmapEventDto(
    @SerializedName("event_id") val eventId: String,
    @SerializedName("course_id") val courseId: String,
    @SerializedName("course_name") val courseName: String,
    @SerializedName("title") val title: String,
    @SerializedName("event_type") val eventType: String,
    @SerializedName("event_date") val eventDate: String
)

fun WeeklyRoadmapDto.toDomain() = WeeklyRoadmap(
    roadmapStartDate = roadmapStartDate,
    weeks = weeks.map { it.toDomain() }
)

fun RoadmapWeekDto.toDomain() = RoadmapWeek(
    weekNumber = weekNumber,
    startDate = startDate,
    endDate = endDate,
    studyBlocks = studyBlocks.map { it.toDomain() }
)

fun StudyBlockDto.toDomain() = StudyBlock(
    id = blockId,
    courseId = courseId,
    courseName = courseName,
    tasks = tasks.map { it.toDomain() },
    events = events.map { it.toDomain() },
    isCompleted = isCompleted
)

fun RoadmapTaskDto.toDomain() = RoadmapTask(
    topic = topic,
    durationMinutes = durationMinutes,
    taskDate = taskDate
)

fun RoadmapEventDto.toDomain() = RoadmapEvent(
    id = eventId,
    courseId = courseId,
    courseName = courseName,
    title = title,
    eventType = eventType,
    eventDate = eventDate
)
