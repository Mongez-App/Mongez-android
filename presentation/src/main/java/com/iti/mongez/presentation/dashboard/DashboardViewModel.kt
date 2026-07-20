package com.iti.mongez.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.screens.dashboard.TaskPriority
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.dashboard.usecase.GetDashboardDataUseCase
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
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState(isLoading = true))
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DashboardEffect>()
    val effect: SharedFlow<DashboardEffect> = _effect.asSharedFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // Mocking the API response for now
            _state.value = _state.value.copy(
                isLoading = false,
                streakCount = 5,
                isStreakActive = true,
                focusTopic = "Algorithms",
                focusDuration = "90 min",
                goals = listOf(
                    GoalItem("Today's Goal", 3, 5, "tasks", GoalType.DAILY),
                    GoalItem("Weekly Progress", 12, 20, "hours", GoalType.WEEKLY),
                    GoalItem("Monthly", 45, 80, "hours", GoalType.MONTHLY)
                ),
                tasks = listOf(
                    TaskItem("1", "Process Management", "60 min", TaskPriority.HIGH, true),
                    TaskItem("2", "Dynamic Programming", "90 min", TaskPriority.MEDIUM, false),
                    TaskItem("3", "TCP/IP Stack", "45 min", TaskPriority.LOW, false)
                ),
                deadlines = listOf(
                    DeadlineItem("1", "Algorithms", "Midterm Exam", "Tomorrow", true),
                    DeadlineItem("2", "Database Systems", "Project Submission", "In 3 Days", false)
                )
            )

            /*
            getDashboardDataUseCase().fold(
                onSuccess = { data ->
                    val summary = data.summary

                    _state.value = _state.value.copy(
                        isLoading = false,
                        streakCount = data.streak,
                        isStreakActive = data.streak > 0,
                        focusTopic = summary.focus?.courseName ?: "No Focus Set",
                        focusDuration = "${summary.focus?.durationMinutes ?: 0} min",
                        goals = listOf(
                            GoalItem("Today's Goal", summary.metrics.todayCompletedTasks, summary.metrics.todayTotalTasks, "tasks", GoalType.DAILY),
                            GoalItem("Weekly Progress", summary.metrics.weeklyHoursCompleted, summary.metrics.weeklyHoursGoal, "hours", GoalType.WEEKLY),
                            GoalItem("Monthly", summary.metrics.monthlyHoursCompleted, summary.metrics.monthlyHoursGoal, "hours", GoalType.MONTHLY)
                        ),
                        tasks = summary.tasks.map { task ->
                            TaskItem(
                                id = task.id,
                                title = task.title,
                                duration = "${task.durationMinutes} min",
                                priority = TaskPriority.valueOf(task.priority), // Ensure enum matching
                                isCompleted = task.isCompleted
                            )
                        },
                        deadlines = summary.deadlines.map { deadline ->
                            DeadlineItem(
                                id = deadline.id,
                                subject = deadline.courseName,
                                taskType = deadline.title,
                                timeLeft = deadline.dueText,
                                isUrgent = deadline.dueText.contains("Tomorrow", ignoreCase = true) || deadline.dueText.contains("Days", ignoreCase = true)
                            )
                        }
                    )
                },
                onFailure = {
                    _state.value = _state.value.copy(isLoading = false)
                    _effect.emit(DashboardEffect.ShowSnackbar("Failed to load dashboard data.", AppSnackbarType.Error))
                },
                onLoading = { /* Handled internally by initial copy */ }
            )
            */
        }
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