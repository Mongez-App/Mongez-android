package com.iti.mongez.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.screens.dashboard.TaskPriority
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState())
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DashboardEffect>()
    val effect: SharedFlow<DashboardEffect> = _effect.asSharedFlow()

    init {
        loadDashboardMockData()
    }

    private fun loadDashboardMockData() {
        _state.value = DashboardUiState(
            userName = "Abdullah",
            greetingSubtext = "Let's hit today's tasks",
            streakCount = 12,
            isStreakActive = true,
            focusTitle = "Today's Focus",
            focusTopic = "Operating\nSystems",
            focusDuration = "2h 15m",
            goals = listOf(
                GoalItem("Today's Goal", 3, 5, "tasks", GoalType.DAILY),
                GoalItem("Weekly Progress", 12, 20, "hours", GoalType.WEEKLY),
                GoalItem("Monthly", 45, 80, "tasks", GoalType.MONTHLY)
            ),
            tasks = listOf(
                TaskItem("1", "Read Chapter 4", "45 min", TaskPriority.HIGH, isCompleted = true),
                TaskItem("2", "Practice DFS Problems", "30 min", TaskPriority.MEDIUM, isCompleted = false),
                TaskItem("3", "Finish Quiz", "20 min", TaskPriority.LOW, isCompleted = false)
            ),
            deadlines = listOf(
                DeadlineItem("1", "Networks", "Assignment", "Tomorrow", isUrgent = true),
                DeadlineItem("2", "Operating Systems", "Midterm", "4 days left", isUrgent = false)
            )
        )
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.OnTaskCheckedToggled -> {
                val updatedTasks = _state.value.tasks.map { task ->
                    if (task.id == event.taskId) task.copy(isCompleted = event.isCompleted) else task
                }
                _state.value = _state.value.copy(tasks = updatedTasks)

                // Example of raising side-effects via MVI Effect architecture
                viewModelScope.launch {
                    if (event.isCompleted) {
                        _effect.emit(DashboardEffect.ShowSnackbar("Task marked completed!", AppSnackbarType.Success))                    }
                }
            }
            is DashboardEvent.OnStartFocusClicked -> {
                viewModelScope.launch {
                    _effect.emit(DashboardEffect.NavigateToFocusSession)
                }
            }
            is DashboardEvent.OnViewAllTasksClicked -> {
                viewModelScope.launch { _effect.emit(DashboardEffect.NavigateToAllTasks) }
            }
            is DashboardEvent.OnViewAllDeadlinesClicked -> {
                viewModelScope.launch { _effect.emit(DashboardEffect.NavigateToAllDeadlines) }
            }
        }
    }
}