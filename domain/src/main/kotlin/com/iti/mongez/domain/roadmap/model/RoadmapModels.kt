package com.iti.mongez.domain.roadmap.model

data class WeeklyRoadmap(
    val roadmapStartDate: String,
    val weeks: List<RoadmapWeek>
)

data class RoadmapWeek(
    val weekNumber: Int,
    val startDate: String,
    val endDate: String,
    val days: List<RoadmapDay>
)

data class RoadmapDay(
    val date: String,
    val dayName: String,
    val studyBlocks: List<StudyBlock>
)

data class StudyBlock(
    val id: String,
    val courseName: String,
    val topic: String,
    val durationMinutes: Int,
    val isCompleted: Boolean,
    val event: RoadmapEvent? = null
)

data class RoadmapEvent(
    val title: String,
    val type: String
)
