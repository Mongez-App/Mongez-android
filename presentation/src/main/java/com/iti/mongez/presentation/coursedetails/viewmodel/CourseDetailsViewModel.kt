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
class CourseDetailsViewModel @Inject constructor(
    private val getCourseDetailsUseCase: GetCourseDetailsUseCase,
    private val getCourseMaterialsUseCase: GetCourseMaterialsUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase,
    private val deleteCourseMaterialUseCase: DeleteCourseMaterialUseCase,
    private val uploadCourseMaterialUseCase: UploadCourseMaterialUseCase
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

            when (val materialsResult = getCourseMaterialsUseCase(courseId)) {
                is Result.Success -> {
                    val documents = materialsResult.data.map { mat ->
                        val finalId = if (mat.id.isBlank()) {
                            Log.e("CourseDebug", "WARNING: Backend sent a blank ID for file: ${mat.name}. Generating fake UUID.")
                            java.util.UUID.randomUUID().toString()
                        } else {
                            mat.id
                        }

                        DocumentItem(
                            id = finalId,
                            title = mat.name,
                            pageCount = mat.pageCount,
                            fileSize = "${mat.fileSizeMb} MB"
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
            is CourseDetailsIntent.SelectTab -> {
                _uiState.update { it.copy(selectedTabIndex = intent.index) }
            }
            is CourseDetailsIntent.DeleteDocument -> {
                deleteDocument(intent.documentId)
            }
            is CourseDetailsIntent.ShowDeleteDialog -> {
                _uiState.update { it.copy(isDeleteDialogVisible = true) }
            }
            is CourseDetailsIntent.DismissDeleteDialog -> {
                _uiState.update { it.copy(isDeleteDialogVisible = false) }
            }
            is CourseDetailsIntent.DeleteCourse -> {
                deleteCourse()
            }
            is CourseDetailsIntent.SelectTaskFilter -> {
                _uiState.update { it.copy(selectedTaskFilterIndex = intent.index) }
            }
            is CourseDetailsIntent.ClickTask -> {
                // Toggle task completion
            }
            else -> {}
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

    fun uploadFile(fileName: String, contentType: String, fileSizeBytes: Long, pageCount: Int, fileBytes: ByteArray) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = uploadCourseMaterialUseCase(
                courseId, fileName, contentType, fileSizeBytes, pageCount, fileBytes
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