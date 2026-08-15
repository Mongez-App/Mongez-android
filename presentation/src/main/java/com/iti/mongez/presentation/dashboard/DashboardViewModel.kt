package com.iti.mongez.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.screens.dashboard.TaskPriority
import com.iti.mongez.domain.dashboard.usecase.GetDashboardDataUseCase
import com.iti.mongez.domain.auth.repository.AuthRepository
import com.iti.mongez.presentation.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getDashboardDataUseCase: GetDashboardDataUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DashboardUiState(isLoading = true))
    val state: StateFlow<DashboardUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<DashboardEffect>()
    val effect: SharedFlow<DashboardEffect> = _effect.asSharedFlow()

    init {
        observeUserProfile()
        loadDashboardData()
    }

    private fun observeUserProfile() {
        authRepository.getCurrentUserFlow()
            .onEach { user ->
                user?.let {
                    _state.value = _state.value.copy(
                        userName = it.name.ifEmpty { "User" },
                        avatarUrl = it.avatarUrl
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            getDashboardDataUseCase().fold(
                onSuccess = { aggregatedData ->
                    android.util.Log.d("DASHBOARD_DEBUG", "Success! Streak: ${aggregatedData.streak}")
                    android.util.Log.d("DASHBOARD_DEBUG", "Tasks Count: ${aggregatedData.summary.tasks.size}")
                    android.util.Log.d("DASHBOARD_DEBUG", "Focus: ${aggregatedData.summary.focus}")
                    val summary = aggregatedData.summary
                    _state.value = _state.value.copy(
                        isLoading = false,
                        userName = aggregatedData.userName.ifEmpty { "User" },
                        avatarUrl = aggregatedData.avatarUrl,
                        welcomeMessage = summary.welcomeMessage,
                        streakCount = if (summary.streakDays > 0) summary.streakDays else aggregatedData.streak,
                        isStreakActive = (summary.streakDays > 0 || aggregatedData.streak > 0),
                        todayFocus = summary.focus?.takeIf { it.courseName.isNotBlank() }?.let { focus ->
                            FocusItem(
                                courseId = focus.courseId,
                                courseName = focus.courseName,
                                durationText = focus.allocatedDuration.ifEmpty { "${focus.durationMinutes} min" }
                            )
                        },
                        goals = listOf(
                            GoalItem(R.string.dashboard_daily_goal, summary.metrics.todayCompletedTasks, summary.metrics.todayTotalTasks, R.string.tasks, GoalType.DAILY),
                            GoalItem(R.string.dashboard_weekly_goal, summary.metrics.weeklyHoursCompleted, summary.metrics.weeklyHoursGoal, R.string.hours, GoalType.WEEKLY),
                            GoalItem(R.string.dashboard_monthly_goal, summary.metrics.monthlyHoursCompleted, summary.metrics.monthlyHoursGoal, R.string.hours, GoalType.MONTHLY)
                        ),
                        tasks = summary.tasks.map { task ->
                            TaskItem(
                                id = task.id,
                                courseId = task.courseId,
                                title = task.title,
                                duration = "${task.durationMinutes} min",
                                durationMinutes = task.durationMinutes,
                                priority = try {
                                    TaskPriority.valueOf(task.priority.uppercase())
                                } catch (e: Exception) {
                                    TaskPriority.LOW
                                },
                                isCompleted = task.isCompleted
                            )
                        },
                        deadlines = summary.deadlines.map { deadline ->
                            DeadlineItem(
                                id = deadline.id,
                                subject = deadline.courseName,
                                taskType = deadline.title,
                                timeLeft = deadline.dueText,
                                isUrgent = deadline.dueText.contains("Tomorrow", ignoreCase = true) || deadline.dueText.contains("Day", ignoreCase = true)
                            )
                        },
                        aiSuggestionText = summary.aiSuggestion
                    )
                },
                onFailure = {
                    android.util.Log.e("DASHBOARD_DEBUG", "Dashboard Failure Exception: ${it.message}")
                    _state.value = _state.value.copy(isLoading = false)
                    _effect.emit(DashboardEffect.ShowSnackbar(R.string.dashboard_error_loading, AppSnackbarType.Error))
                },
                onLoading = {
                    _state.value = _state.value.copy(isLoading = true)
                }
            )
        }
    }

    fun onEvent(event: DashboardEvent) {
        when (event) {
            is DashboardEvent.OnTaskCheckedToggled -> {
                val updatedTasks = _state.value.tasks.map { task ->
                    if (task.id == event.taskId) task.copy(isCompleted = event.isCompleted) else task
                }
                _state.value = _state.value.copy(tasks = updatedTasks)

                viewModelScope.launch {
                    if (event.isCompleted) {
                        _effect.emit(DashboardEffect.ShowSnackbar(R.string.task_marked_completed, AppSnackbarType.Success))
                    }
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
    fun refreshData() {
        loadDashboardData()
    }
}