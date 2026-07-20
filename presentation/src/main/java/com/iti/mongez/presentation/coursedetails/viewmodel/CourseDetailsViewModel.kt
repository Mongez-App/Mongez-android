package com.iti.mongez.feature.coursedetails.viewmodel

import androidx.lifecycle.ViewModel
import com.iti.mongez.presentation.coursedetails.contract.CourseDetailsIntent
import com.iti.mongez.presentation.coursedetails.uistate.CourseDetailsUiState
import com.iti.mongez.presentation.coursedetails.uistate.DocumentItem
import com.iti.mongez.presentation.coursedetails.uistate.TaskItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CourseDetailsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(CourseDetailsUiState(
        tasks = listOf(
            TaskItem(
                id = "1",
                title = "Read Chapter 4",
                duration = "45 min",
                priority = "HIGH",
                isCompleted = true,
                isToday = true
            ),
            TaskItem(
                id = "2",
                title = "Practice DFS Problems",
                duration = "30 min",
                priority = "MEDIUM",
                isCompleted = false,
                isToday = true
            ),
            TaskItem(
                id = "3",
                title = "Finish Quiz",
                duration = "20 min",
                priority = "LOW",
                isCompleted = false,
                isToday = true
            ),
            TaskItem(
                id = "4",
                title = "Practice DFS Problems",
                duration = "30 min",
                priority = "MEDIUM",
                isCompleted = false,
                isToday = false
            ),
            TaskItem(
                id = "5",
                title = "Read Chapter 4",
                duration = "45 min",
                priority = "HIGH",
                isCompleted = false,
                isToday = false
            )
        )
    ))
    val uiState: StateFlow<CourseDetailsUiState> = _uiState.asStateFlow()

    init {
        loadMockData()
    }

    fun processIntent(intent: CourseDetailsIntent) {
        when (intent) {
            is CourseDetailsIntent.SelectTab -> {
                _uiState.update { it.copy(selectedTabIndex = intent.index) }
            }
            is CourseDetailsIntent.ClickBack -> {
                // Handled in UI navigation callback
            }
            is CourseDetailsIntent.EditCourse -> {
                // Handle edit course navigation/action
            }
            is CourseDetailsIntent.DeleteCourse -> {
                // Handle delete course action
            }
            is CourseDetailsIntent.ClickDocument -> {
                // Handle opening document
            }
            is CourseDetailsIntent.DeleteDocument -> {
                _uiState.update { state ->
                    state.copy(materials = state.materials.filter { it.id != intent.documentId })
                }
            }
            is CourseDetailsIntent.ClickUploadMaterial -> {
                // Handle upload action
            }
            is CourseDetailsIntent.SelectTaskFilter -> {
                _uiState.update { it.copy(selectedTaskFilterIndex = intent.index) }
            }
            is CourseDetailsIntent.ClickTask -> {
                _uiState.update { state ->
                    val updatedTasks = state.tasks.map {
                        if (it.id == intent.taskId) {
                            it.copy(isCompleted = !it.isCompleted)
                        } else {
                            it
                        }
                    }
                    state.copy(tasks = updatedTasks)
                }
            }
        }
    }

    private fun loadMockData() {
        _uiState.update { currentState ->
            currentState.copy(
                materials = listOf(
                    DocumentItem("1", "Chapter 1 - Introduction.pdf", 28, "2.4 MB"),
                    DocumentItem("2", "Chapter 2 - Processes.pdf", 45, "3.1 MB"),
                    DocumentItem("3", "Chapter 3 - Memory.pdf", 36, "2.8 MB"),
                    DocumentItem("4", "Chapter 4 - CPU Scheduling.pdf", 29, "2.2 MB")
                )
            )
        }
    }
}