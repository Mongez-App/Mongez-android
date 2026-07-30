package com.iti.mongez.presentation.roadmap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.domain.roadmap.usecase.GetWeeklyRoadmapUseCase
import com.iti.mongez.presentation.roadmap.contract.RoadmapEffect
import com.iti.mongez.presentation.roadmap.contract.RoadmapEvent
import com.iti.mongez.presentation.roadmap.uiState.RoadmapDayUiModel
import com.iti.mongez.presentation.roadmap.uiState.RoadmapEventUiModel
import com.iti.mongez.presentation.roadmap.uiState.RoadmapTaskUiModel
import com.iti.mongez.presentation.roadmap.uiState.RoadmapUiState
import com.iti.mongez.presentation.roadmap.uiState.RoadmapWeekUiModel
import com.iti.mongez.presentation.roadmap.uiState.StudyBlockUiModel
import com.iti.mongez.presentation.roadmap.uiState.StudyBlockColor
import com.iti.mongez.presentation.utils.UiText
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.roadmap.uiState.RoadmapFilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.time.Instant
import java.time.ZoneId

@HiltViewModel
class RoadmapViewModel @Inject constructor(
    private val getWeeklyRoadmapUseCase: GetWeeklyRoadmapUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RoadmapUiState(isLoading = true))
    val state: StateFlow<RoadmapUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RoadmapEffect>()
    val effect: SharedFlow<RoadmapEffect> = _effect.asSharedFlow()

    private var fullRoadmapWeeks: List<RoadmapWeekUiModel> = emptyList()
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())

    init {
        loadRoadmap()
    }

    private fun loadRoadmap() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            getWeeklyRoadmapUseCase().fold(
                onSuccess = { roadmap ->
                    val courseColors = mutableMapOf<String, StudyBlockColor>()
                    val colors = StudyBlockColor.entries.toTypedArray()
                    var colorIndex = 0

                    val uiWeeks = roadmap.weeks.map { week ->
                        // Extract all unique dates from tasks and events in this week
                        val allDates = week.studyBlocks.flatMap { block ->
                            block.tasks.map { it.taskDate } + block.events.map { 
                                // Event date is ISO 8601, extract date part
                                it.eventDate.substringBefore('T')
                            }
                        }.distinct().sorted()

                        val uiDays = allDates.map { dateStr ->
                            val date = LocalDate.parse(dateStr)
                            val dayName = date.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())

                            val uiBlocks = week.studyBlocks.mapNotNull { block ->
                                val dayTasks = block.tasks.filter { it.taskDate == dateStr }
                                val dayEvents = block.events.filter { it.eventDate.substringBefore('T') == dateStr }

                                if (dayTasks.isEmpty() && dayEvents.isEmpty()) return@mapNotNull null

                                val color = courseColors.getOrPut(block.courseName) {
                                    val selectedColor = colors[colorIndex % colors.size]
                                    colorIndex++
                                    selectedColor
                                }

                                StudyBlockUiModel(
                                    id = block.id,
                                    courseName = UiText.DynamicString(block.courseName),
                                    topic = UiText.DynamicString(dayTasks.firstOrNull()?.topic ?: block.courseName),
                                    durationMinutes = dayTasks.sumOf { it.durationMinutes },
                                    isCompleted = block.isCompleted,
                                    color = color,
                                    tasks = dayTasks.map { task ->
                                        RoadmapTaskUiModel(
                                            title = UiText.DynamicString(task.topic),
                                            dateTime = UiText.DynamicString("${task.durationMinutes} min")
                                        )
                                    },
                                    events = dayEvents.map { event ->
                                        RoadmapEventUiModel(
                                            title = UiText.DynamicString(event.title),
                                            type = event.eventType,
                                            dateTime = UiText.DynamicString(
                                                event.eventDate.substringAfter('T').substringBefore(':') + ":" +
                                                event.eventDate.substringAfter(':').substringBefore(':')
                                            )
                                        )
                                    }
                                )
                            }

                            RoadmapDayUiModel(
                                date = dateStr,
                                dayName = UiText.DynamicString(dayName),
                                blocks = uiBlocks
                            )
                        }

                        val startDate = LocalDate.parse(week.startDate)
                        val endDate = LocalDate.parse(week.endDate)
                        val rangeFormatter = DateTimeFormatter.ofPattern("MMM d")
                        val dateRange = "${startDate.format(rangeFormatter)} - ${endDate.format(rangeFormatter)}"

                        RoadmapWeekUiModel(
                            weekNumber = week.weekNumber,
                            dateRange = UiText.DynamicString(dateRange),
                            days = uiDays
                        )
                    }

                    val allCourses = uiWeeks.flatMap { week -> 
                        week.days.flatMap { day -> 
                            day.blocks.map { it.courseName.asRawString() }
                        }
                    }.distinct().sorted()

                    val allEventTypes = uiWeeks.flatMap { week ->
                        week.days.flatMap { day ->
                            day.blocks.flatMap { it.events.map { event -> event.type } }
                        }
                    }.distinct().sorted().ifEmpty { listOf("Study", "Assignment", "Quiz", "Exam", "Reminder") }

                    fullRoadmapWeeks = uiWeeks
                    _state.value = _state.value.copy(
                        isLoading = false,
                        roadmapStartDate = roadmap.roadmapStartDate,
                        weeks = uiWeeks,
                        availableCourses = allCourses,
                        availableEventTypes = allEventTypes
                    )
                },
                onFailure = { error ->
                    _state.value = _state.value.copy(isLoading = false)
                    _effect.emit(
                        RoadmapEffect.ShowSnackBar(
                            message = error.message ?: "Failed to load roadmap",
                            type = AppSnackbarType.Error
                        )
                    )
                },
                onLoading = {
                    _state.value = _state.value.copy(isLoading = true)
                }
            )
        }
    }

    fun onEvent(event: RoadmapEvent) {
        viewModelScope.launch {
            when (event) {
                is RoadmapEvent.LoadRoadmap -> loadRoadmap()
                is RoadmapEvent.OnBlockClicked -> {
                    _effect.emit(RoadmapEffect.NavigateToBlockDetails(event.blockId))
                }
                is RoadmapEvent.OnAddEventClicked -> {
                    _state.value = _state.value.copy(isAddEventDialogVisible = true)
                }
                is RoadmapEvent.ToggleFilterSheet -> {
                    _state.value = _state.value.copy(isFilterSheetVisible = event.isVisible)
                }
                is RoadmapEvent.ToggleAddEventDialog -> {
                    _state.value = _state.value.copy(isAddEventDialogVisible = event.isVisible)
                }
                is RoadmapEvent.ApplyFilter -> {
                    _state.value = _state.value.copy(
                        isFilterSheetVisible = false,
                        activeFilterState = event.filterState
                    )
                    applyFilters(event.filterState)
                }
                is RoadmapEvent.ClearAllFilters -> {
                    val clearedState = RoadmapFilterState()
                    _state.value = _state.value.copy(activeFilterState = clearedState)
                    applyFilters(clearedState)
                }
                is RoadmapEvent.RemoveDateFilter -> {
                    val newState = _state.value.activeFilterState.copy(startDate = null, endDate = null)
                    _state.value = _state.value.copy(activeFilterState = newState)
                    applyFilters(newState)
                }
                is RoadmapEvent.RemoveCourseFilter -> {
                    val newState = _state.value.activeFilterState.copy(
                        selectedCourses = _state.value.activeFilterState.selectedCourses - event.course
                    )
                    _state.value = _state.value.copy(activeFilterState = newState)
                    applyFilters(newState)
                }
                is RoadmapEvent.RemoveEventTypeFilter -> {
                    val newState = _state.value.activeFilterState.copy(
                        selectedEventTypes = _state.value.activeFilterState.selectedEventTypes - event.eventType
                    )
                    _state.value = _state.value.copy(activeFilterState = newState)
                    applyFilters(newState)
                }
                is RoadmapEvent.AddEvent -> {
                    _effect.emit(
                        RoadmapEffect.ShowSnackBar(
                            message = "Event \"${event.name}\" created successfully!",
                            type = AppSnackbarType.Success
                        )
                    )
                    _state.value = _state.value.copy(isAddEventDialogVisible = false)
                     loadRoadmap()
                }
            }
        }
    }

    private fun applyFilters(filterState: RoadmapFilterState) {
        val filteredWeeks = fullRoadmapWeeks.mapNotNull { week ->
            val filteredDays = week.days.mapNotNull { day ->
                val dayDate = java.time.LocalDate.parse(day.date, dateFormatter)

                // 1. Date Filter Check
                val matchesDate = if (filterState.startDate != null && filterState.endDate != null) {
                    !dayDate.isBefore(filterState.startDate) && !dayDate.isAfter(filterState.endDate)
                } else {
                    true
                }

                if (!matchesDate) return@mapNotNull null

                // 2 & 3. Course and Event Type Checks
                val filteredBlocks = day.blocks.filter { block ->
                    val matchesCourse = filterState.selectedCourses.isEmpty() ||
                            filterState.selectedCourses.contains(block.courseName.asRawString())

                    val blockEventTypes = block.events.map { it.type }.ifEmpty { listOf("Study") }
                    val matchesEventType = filterState.selectedEventTypes.isEmpty() ||
                            blockEventTypes.any { it in filterState.selectedEventTypes }

                    matchesCourse && matchesEventType
                }

                if (filteredBlocks.isNotEmpty()) {
                    day.copy(blocks = filteredBlocks)
                } else {
                    null
                }
            }

            if (filteredDays.isNotEmpty()) {
                week.copy(days = filteredDays)
            } else {
                null
            }
        }

        _state.value = _state.value.copy(weeks = filteredWeeks)
    }
}
