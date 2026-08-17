package com.iti.mongez.presentation.teamcourses.model

// Presentation model to hold the event data shown in the grid
data class TrackEvent(
    val id: String,
    val courseName: String,
    val eventName: String,
    val timeRemaining: String,
    val eventType: String
)
