package com.iti.mongez.presentation.courses.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.domain.courses.model.CourseCreationResult
import com.iti.mongez.domain.courses.usecase.CreateCourseUseCase
import com.iti.mongez.domain.courses.usecase.DeleteCourseUseCase
import com.iti.mongez.domain.courses.usecase.GetCoursesUseCase
import com.iti.mongez.domain.courses.usecase.UploadCourseMaterialUseCase
import com.iti.mongez.presentation.courses.contract.CoursesEffect
import com.iti.mongez.presentation.courses.contract.CoursesIntent
import com.iti.mongez.presentation.courses.uiState.CoursesState
import com.iti.mongez.presentation.utils.getFileInfo
import com.iti.mongez.presentation.utils.readBytes
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val getCoursesUseCase: GetCoursesUseCase,
    private val createCourseUseCase: CreateCourseUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase,
    private val uploadCourseMaterialUseCase: UploadCourseMaterialUseCase
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
            is CoursesIntent.ShowDeleteConfirmation -> {
                _state.update { it.copy(courseToDeleteId = intent.courseId) }
            }
            CoursesIntent.DismissDeleteConfirmation -> {
                _state.update { it.copy(courseToDeleteId = null) }
            }
            is CoursesIntent.ConfirmDeleteCourse -> {
                deleteCourse(intent.courseId)
            }
            CoursesIntent.FilterClicked -> { /* Filter logic */ }
            is CoursesIntent.ShowSnackbar -> {
                viewModelScope.launch {
                    _effect.emit(CoursesEffect.ShowSnackbar(intent.message, intent.type))
                }
            }
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
                    _effect.emit(CoursesEffect.ShowSnackbar(result.exception.message ?: "Failed to load courses", AppSnackbarType.Error))
                }
                else -> {}
            }
        }
    }

    private fun deleteCourse(courseId: String) {
        viewModelScope.launch {
            // Dismiss dialog immediately and mark deleting state
            _state.update { it.copy(isDeletingCourse = true, courseToDeleteId = null) }

            // 1. INSTANT OPTIMISTIC UPDATE: Remove course immediately from local lists
            val updatedAll = _state.value.allCourses.filter { it.id != courseId }
            val updatedFiltered = _state.value.filteredCourses.filter { it.id != courseId }
            _state.update {
                it.copy(
                    allCourses = updatedAll,
                    filteredCourses = updatedFiltered
                )
            }

            // 2. Perform backend deletion request
            when (val result = deleteCourseUseCase(courseId)) {
                is Result.Success -> {
                    _effect.emit(CoursesEffect.ShowSnackbar("Course deleted successfully", AppSnackbarType.Success))
                    _effect.emit(CoursesEffect.NavigateBack) // Returns to courses list instantly
                }
                is Result.Failure -> {
                    // Rollback list if deletion fails on server
                    loadCourses()
                    _effect.emit(CoursesEffect.ShowSnackbar(result.exception.message ?: "Failed to delete course", AppSnackbarType.Error))
                }
                else -> {
                    loadCourses()
                }
            }

            _state.update { it.copy(isDeletingCourse = false) }
        }
    }

    private fun createCourse(intent: CoursesIntent.CreateCourse) {
        viewModelScope.launch {
            _state.update { it.copy(isCreatingCourse = true) }

            val result = createCourseUseCase(
                name = intent.name,
                courseCode = intent.courseCode,
                imageUrl = intent.imageUrl,
                startDate = intent.startDate,
                examDate = intent.examDate,
                courseType = intent.courseType,
                materialUrl = intent.materialUrl
            )

            when (result) {
                is Result.Success<*> -> {
                    val creationResult = result.data as CourseCreationResult
                    val courseId = creationResult.course.id

                    _state.update { it.copy(isAddCourseSheetVisible = false) }

                    if (intent.materials.isNotEmpty()) {
                        _effect.emit(CoursesEffect.ShowSnackbar("Course created. Uploading materials...", AppSnackbarType.Info))

                        var uploadError = false
                        intent.materials.forEach { uri ->
                            val fileInfo = uri.getFileInfo(context)
                            val fileBytes = uri.readBytes(context)

                            if (fileBytes != null) {
                                val uploadResult = uploadCourseMaterialUseCase(
                                    courseId = courseId,
                                    fileName = fileInfo.name,
                                    contentType = fileInfo.mimeType,
                                    fileSizeBytes = fileInfo.sizeBytes,
                                    pageCount = 1,
                                    fileBytes = fileBytes,
                                    deviceFileUri = uri.toString()
                                )
                                if (uploadResult is Result.Failure) {
                                    uploadError = true
                                }
                            }
                        }

                        if (uploadError) {
                            _effect.emit(CoursesEffect.ShowSnackbar("Some materials failed to upload.", AppSnackbarType.Error))
                        } else {
                            _effect.emit(CoursesEffect.ShowSnackbar("Course and materials uploaded successfully!", AppSnackbarType.Success))
                        }
                    } else {
                        creationResult.alertMessage?.let { alert ->
                            _effect.emit(CoursesEffect.ShowSnackbar(message = alert, type = AppSnackbarType.Success))
                        }
                    }

                    _state.update { it.copy(isCreatingCourse = false) }
                    loadCourses()
                }
                is Result.Failure -> {
                    _state.update { it.copy(isCreatingCourse = false) }
                    _effect.emit(CoursesEffect.ShowSnackbar(result.exception.message ?: "Failed to create course", AppSnackbarType.Error))
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