package com.iti.mongez.feature.coursedetails.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.courses.usecase.*
import com.iti.mongez.presentation.coursedetails.contract.CourseDetailsEffect
import com.iti.mongez.presentation.coursedetails.contract.CourseDetailsIntent
import com.iti.mongez.presentation.coursedetails.uistate.CourseDetailsUiState
import com.iti.mongez.presentation.coursedetails.uistate.DocumentItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseDetailsViewModel @Inject constructor(
    private val getCourseDetailsUseCase: GetCourseDetailsUseCase,
    private val getCourseMaterialsUseCase: GetCourseMaterialsUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase,
    private val deleteCourseMaterialUseCase: DeleteCourseMaterialUseCase,
    private val uploadCourseMaterialUseCase: UploadCourseMaterialUseCase,
    private val updateCourseUseCase: UpdateCourseUseCase
) : ViewModel() {

    private var courseId: String = ""

    private val _uiState = MutableStateFlow(CourseDetailsUiState())
    val uiState: StateFlow<CourseDetailsUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CourseDetailsEffect>()
    val effect: SharedFlow<CourseDetailsEffect> = _effect.asSharedFlow()

    private fun loadCourseData() {
        if (courseId.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // 1. Fetch Course Details
            when (val detailsResult = getCourseDetailsUseCase(courseId)) {
                is Result.Success -> {
                    val course = detailsResult.data
                    _uiState.update { state ->
                        state.copy(
                            courseTitle = course.name,
                            courseCode = course.courseCode,
                            courseType = course.courseType,
                            startDate = course.startDate,
                            examDate = course.examDate,
                            materialUrl = course.materialUrl,
                            imageUrl = course.imageUrl ?: ""
                        )
                    }
                }
                is Result.Failure -> {
                    Log.e("CourseDebug", "Failed to load details: ${detailsResult.exception?.message}")
                    _effect.emit(CourseDetailsEffect.ShowSnackbar("Failed to load course details", AppSnackbarType.Error))
                }
                else -> {}
            }

            // 2. Fetch Course Materials
            when (val materialsResult = getCourseMaterialsUseCase(courseId)) {
                is Result.Success -> {
                    val documents = materialsResult.data.map { mat ->
                        val finalId = if (mat.id.isBlank()) {
                            java.util.UUID.randomUUID().toString()
                        } else mat.id

                        DocumentItem(
                            id = finalId,
                            title = mat.name,
                            pageCount = mat.pageCount,
                            fileSize = "${mat.fileSizeMb} MB",
                            fileUri = mat.deviceFileUri
                        )
                    }
                    _uiState.update { it.copy(materials = documents, isLoading = false) }
                }
                is Result.Failure -> {
                    Log.e("CourseDebug", "Failed to load materials: ${materialsResult.exception?.message}")
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(CourseDetailsEffect.ShowSnackbar("Failed to load materials", AppSnackbarType.Error))
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun processIntent(intent: CourseDetailsIntent) {
        when (intent) {
            is CourseDetailsIntent.LoadCourse -> {
                if (this.courseId != intent.courseId) {
                    this.courseId = intent.courseId
                    loadCourseData()
                }
            }
            is CourseDetailsIntent.SelectTab -> _uiState.update { it.copy(selectedTabIndex = intent.index) }
            is CourseDetailsIntent.ClickDocument -> openDocument(intent.documentId) // CONNECTED HERE
            is CourseDetailsIntent.DeleteDocument -> deleteDocument(intent.documentId)
            is CourseDetailsIntent.ShowDeleteDialog -> _uiState.update { it.copy(isDeleteDialogVisible = true) }
            is CourseDetailsIntent.DismissDeleteDialog -> _uiState.update { it.copy(isDeleteDialogVisible = false) }
            is CourseDetailsIntent.DeleteCourse -> deleteCourse()
            is CourseDetailsIntent.SelectTaskFilter -> _uiState.update { it.copy(selectedTaskFilterIndex = intent.index) }
            is CourseDetailsIntent.ClickTask -> { /* Toggle task completion */ }
            is CourseDetailsIntent.UpdateCourse -> updateCourse(intent.name, intent.imageUrl)
            else -> {}
        }
    }

    private fun openDocument(documentId: String) {
        val document = _uiState.value.materials.find { it.id == documentId }
        val uri = document?.fileUri

        if (!uri.isNullOrBlank()) {
            viewModelScope.launch {
                _effect.emit(CourseDetailsEffect.OpenPdf(uri))
            }
        } else {
            viewModelScope.launch {
                _effect.emit(CourseDetailsEffect.ShowSnackbar("File URI is unavailable", AppSnackbarType.Error))
            }
        }
    }

    private fun updateCourse(name: String, imageUrl: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = updateCourseUseCase(courseId, name, imageUrl, false)) {
                is Result.Success -> {
                    _effect.emit(CourseDetailsEffect.ShowSnackbar("Course updated successfully", AppSnackbarType.Success))
                    loadCourseData()
                }
                is Result.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(CourseDetailsEffect.ShowSnackbar(result.exception?.message ?: "Update failed", AppSnackbarType.Error))
                }
                else -> { _uiState.update { it.copy(isLoading = false) } }
            }
        }
    }

    private fun deleteDocument(documentId: String) {
        viewModelScope.launch {
            when (val result = deleteCourseMaterialUseCase(courseId, documentId)) {
                is Result.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            materials = state.materials.filter { it.id != documentId },
                            isLoading = false
                        )
                    }
                    _effect.emit(CourseDetailsEffect.ShowSnackbar("Material deleted successfully", AppSnackbarType.Success))
                }
                is Result.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(CourseDetailsEffect.ShowSnackbar(result.exception?.message ?: "Failed to delete material", AppSnackbarType.Error))
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun deleteCourse() {
        viewModelScope.launch {
            _uiState.update { it.copy(isDeleteDialogVisible = false, isLoading = true) }
            when (val result = deleteCourseUseCase(courseId)) {
                is Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(CourseDetailsEffect.ShowSnackbar("Course deleted successfully", AppSnackbarType.Success))
                    _effect.emit(CourseDetailsEffect.NavigateBack)
                }
                is Result.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(CourseDetailsEffect.ShowSnackbar(result.exception?.message ?: "Failed to delete course", AppSnackbarType.Error))
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun uploadFile(
        fileName: String,
        contentType: String,
        fileSizeBytes: Long,
        pageCount: Int,
        fileBytes: ByteArray,
        deviceFileUri: String?
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = uploadCourseMaterialUseCase(
                courseId, fileName, contentType, fileSizeBytes, pageCount, fileBytes, deviceFileUri
            )

            when (result) {
                is Result.Success -> {
                    loadCourseData()
                    _effect.emit(CourseDetailsEffect.ShowSnackbar("Material uploaded successfully!", AppSnackbarType.Success))
                }
                is Result.Failure -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(CourseDetailsEffect.ShowSnackbar(result.exception?.message ?: "Upload failed.", AppSnackbarType.Error))
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
}