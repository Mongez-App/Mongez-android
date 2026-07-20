package com.iti.mongez.presentation.courses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.courses.model.CourseCreationResult
import com.iti.mongez.domain.courses.usecase.CreateCourseUseCase
import com.iti.mongez.domain.courses.usecase.GetCoursesUseCase
import com.iti.mongez.presentation.courses.contract.CoursesEffect
import com.iti.mongez.presentation.courses.contract.CoursesIntent
import com.iti.mongez.presentation.courses.uiState.CoursesState
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
class CoursesViewModel @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
    private val createCourseUseCase: CreateCourseUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CoursesState())
    val state: StateFlow<CoursesState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<CoursesEffect>()
    val effect: SharedFlow<CoursesEffect> = _effect.asSharedFlow()

    init {
        handleIntent(CoursesIntent.LoadCourses)
    }

    fun handleIntent(intent: CoursesIntent) {
        when(intent) {
            is CoursesIntent.LoadCourses -> loadCourses()
            is CoursesIntent.SearchQueryChanged -> filterCourses(intent.query)
            is CoursesIntent.CreateCourse -> createCourse(intent)
            is CoursesIntent.ToggleAddCourseSheet -> {
                _state.update { it.copy(isAddCourseSheetVisible = !it.isAddCourseSheetVisible) }
            }
            CoursesIntent.FilterClicked -> { /* Filter logic */ }
        }
    }

    private fun loadCourses() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (val result = getCoursesUseCase()) {
                is Result.Success<*> -> {
                    @Suppress("UNCHECKED_CAST")
                    val courses = result.data as List<Course>
                    _state.update {
                        it.copy(isLoading = false, allCourses = courses, filteredCourses = courses)
                    }
                }
                is Result.Failure -> {
                    _state.update { it.copy(isLoading = false) }
                    // Specify Error type
                    _effect.emit(
                        CoursesEffect.ShowSnackbar(
                            message = result.exception.message ?: "Failed to load courses",
                            type = AppSnackbarType.Error
                        )
                    )
                }

                else -> {}
            }
        }
    }

    private fun createCourse(intent: CoursesIntent.CreateCourse) {
        viewModelScope.launch {
            _state.update { it.copy(isCreatingCourse = true) }
            val result = createCourseUseCase(
                name = intent.name,
                courseCode = intent.courseCode,
                startDate = intent.startDate,
                examDate = intent.examDate,
                hasMaterials = intent.hasMaterials
            )

            _state.update { it.copy(isCreatingCourse = false, isAddCourseSheetVisible = false) }

            when (result) {
                is Result.Success<*> -> {
                    loadCourses() // Refresh list
                    @Suppress("UNCHECKED_CAST")
                    val creationResult = result.data as CourseCreationResult

                    creationResult.alertMessage?.let { alert ->
                        // Specify Success type
                        _effect.emit(
                            CoursesEffect.ShowSnackbar(
                                message = alert,
                                type = AppSnackbarType.Success
                            )
                        )
                    }
                }
                is Result.Failure -> {
                    // Specify Error type
                    _effect.emit(
                        CoursesEffect.ShowSnackbar(
                            message = result.exception.message ?: "Failed to create course",
                            type = AppSnackbarType.Error
                        )
                    )
                }

                else -> {}
            }
        }
    }

    private fun filterCourses(query: String) {
        val filtered = if (query.isEmpty()) {
            _state.value.allCourses
        } else {
            _state.value.allCourses.filter {
                it.name.contains(query, ignoreCase = true) || it.courseCode.contains(query, ignoreCase = true)
            }
        }
        _state.update { it.copy(searchQuery = query, filteredCourses = filtered) }
    }
}