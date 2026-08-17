package com.iti.mongez.presentation.teamcourses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.organization.model.TeamEvent
import com.iti.mongez.domain.organization.usecase.GetTeamCoursesUseCase
import com.iti.mongez.domain.organization.usecase.GetTeamEventsUseCase
import com.iti.mongez.presentation.teamcourses.contract.TrackDetailsEffect
import com.iti.mongez.presentation.teamcourses.contract.TrackDetailsIntent
import com.iti.mongez.presentation.teamcourses.contract.TrackDetailsState
import com.iti.mongez.presentation.teamcourses.model.TrackEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackDetailsViewModel @Inject constructor(
    private val getTeamCoursesUseCase: GetTeamCoursesUseCase,
    private val getTeamEventsUseCase: GetTeamEventsUseCase
) : ViewModel() {

    private var teamId: String = ""

    private val _state = MutableStateFlow(TrackDetailsState())
    val state: StateFlow<TrackDetailsState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<TrackDetailsEffect>()
    val effect: SharedFlow<TrackDetailsEffect> = _effect.asSharedFlow()

    fun onIntent(intent: TrackDetailsIntent) {
        when (intent) {
            is TrackDetailsIntent.LoadTeamDetails -> loadTeamDetails(intent.teamId)
            is TrackDetailsIntent.TabSelected -> {
                _state.update { it.copy(selectedTabIndex = intent.index) }
            }
            is TrackDetailsIntent.SearchQueryChanged -> {
                _state.update {
                    it.copy(
                        searchQuery = intent.query,
                        filteredCourses = filterCourses(it.courses, intent.query)
                    )
                }
            }
            is TrackDetailsIntent.CourseClicked -> {
                viewModelScope.launch { _effect.emit(TrackDetailsEffect.NavigateToCourse(intent.courseId)) }
            }
            is TrackDetailsIntent.EventClicked -> {
                // Handle event click logic
            }
            TrackDetailsIntent.NavigateBack -> {
                viewModelScope.launch { _effect.emit(TrackDetailsEffect.NavigateBack) }
            }
        }
    }

    private fun loadTeamDetails(teamId: String) {
        if (teamId.isBlank() || this.teamId == teamId) return
        this.teamId = teamId

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val coursesDeferred = async { getTeamCoursesUseCase(teamId) }
            val eventsDeferred = async { getTeamEventsUseCase(teamId) }

            val coursesResult = coursesDeferred.await()
            val eventsResult = eventsDeferred.await()

            val courses = (coursesResult as? Result.Success)?.data ?: emptyList()
            val events = (eventsResult as? Result.Success)?.data?.map { it.toTrackEvent() } ?: emptyList()

            _state.update {
                it.copy(
                    isLoading = false,
                    courses = courses,
                    filteredCourses = filterCourses(courses, it.searchQuery),
                    events = events
                )
            }

            (coursesResult as? Result.Failure)?.let {
                _effect.emit(TrackDetailsEffect.ShowError(it.exception.message ?: "Failed to load courses"))
            }
            (eventsResult as? Result.Failure)?.let {
                _effect.emit(TrackDetailsEffect.ShowError(it.exception.message ?: "Failed to load events"))
            }
        }
    }

    private fun filterCourses(courses: List<Course>, query: String): List<Course> {
        if (query.isBlank()) return courses
        return courses.filter { it.name.contains(query, ignoreCase = true) }
    }

    private fun TeamEvent.toTrackEvent() = TrackEvent(
        id = id,
        courseName = courseName,
        eventName = eventType.replaceFirstChar { it.uppercase() },
        timeRemaining = dueText,
        eventType = eventType
    )
}
