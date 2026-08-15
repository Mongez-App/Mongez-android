package com.iti.mongez.presentation.teamcourses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.presentation.teamcourses.contract.TrackDetailsEffect
import com.iti.mongez.presentation.teamcourses.contract.TrackDetailsIntent
import com.iti.mongez.presentation.teamcourses.contract.TrackDetailsState
import com.iti.mongez.presentation.teamcourses.mock.MockData
import dagger.hilt.android.lifecycle.HiltViewModel
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
class TrackDetailsViewModel @Inject constructor() : ViewModel() {

    private val mockCoursesList = MockData.getMockCourses()

    private val _state = MutableStateFlow(
        TrackDetailsState(
            events = MockData.getMockEvents(),
            courses = mockCoursesList,         // Populate courses list
            filteredCourses = mockCoursesList  // Set initial filtered list
        )
    )
    val state: StateFlow<TrackDetailsState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<TrackDetailsEffect>()
    val effect: SharedFlow<TrackDetailsEffect> = _effect.asSharedFlow()

    fun onIntent(intent: TrackDetailsIntent) {
        when (intent) {
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

    private fun filterCourses(courses: List<Course>, query: String): List<Course> {
        if (query.isBlank()) return courses
        return courses.filter { it.name.contains(query, ignoreCase = true) }
    }
}