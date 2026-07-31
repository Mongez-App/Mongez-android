package com.iti.mongez.domain.roadmap.model

data class WeeklyRoadmap(
    val roadmapStartDate: String,
    val weeks: List<RoadmapWeek>
)

data class RoadmapWeek(
    val weekNumber: Int,
    val startDate: String,
    val endDate: String,
    val studyBlocks: List<StudyBlock>
)

data class StudyBlock(
    val id: String,
    val courseId: String,
    val courseName: String,
    val tasks: List<RoadmapTask>,
    val events: List<RoadmapEvent>,
    val isCompleted: Boolean
)

data class RoadmapTask(
    val topic: String,
    val durationMinutes: Int,
    val taskDate: String
)

data class RoadmapEvent(
    val id: String,
    val courseId: String,
    val courseName: String,
    val title: String,
    val eventType: String,
    val eventDate: String
)
