package com.iti.mongez.presentation.teamcourses.contract

import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.presentation.teamcourses.model.TrackEvent

sealed interface TrackDetailsIntent {
    data class LoadTeamDetails(val teamId: String) : TrackDetailsIntent
    data class TabSelected(val index: Int) : TrackDetailsIntent
    data class SearchQueryChanged(val query: String) : TrackDetailsIntent
    object NavigateBack : TrackDetailsIntent
    data class CourseClicked(val courseId: String) : TrackDetailsIntent
    data class EventClicked(val eventId: String) : TrackDetailsIntent
}

sealed interface TrackDetailsEffect {
    object NavigateBack : TrackDetailsEffect
    data class NavigateToCourse(val courseId: String) : TrackDetailsEffect
    data class ShowError(val message: String) : TrackDetailsEffect
}

data class TrackDetailsState(
    val isLoading: Boolean = false,
    val selectedTabIndex: Int = 0,
    val searchQuery: String = "",
    val courses: List<Course> = emptyList(),
    val filteredCourses: List<Course> = emptyList(),
    val events: List<TrackEvent> = emptyList() // Custom model for the events tab
)
