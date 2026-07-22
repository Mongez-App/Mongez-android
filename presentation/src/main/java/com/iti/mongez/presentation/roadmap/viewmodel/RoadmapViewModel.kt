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
            
            // Mocking the API response for now
            val mockWeeks = listOf(
                RoadmapWeekUiModel(
                    weekNumber = 1,
                    dateRange = UiText.DynamicString("May 6 - May 12"),
                    days = listOf(
                        RoadmapDayUiModel(
                            date = "2024-05-06",
                            dayName = UiText.DynamicString("Monday"),
                            blocks = listOf(
                                StudyBlockUiModel(
                                    id = "1",
                                    courseName = UiText.DynamicString("Algorithms"),
                                    topic = UiText.DynamicString("Graph Theory"),
                                    durationMinutes = 60,
                                    isCompleted = true,
                                    color = StudyBlockColor.PURPLE,
                                    events = listOf(
                                        RoadmapEventUiModel(
                                            title = UiText.DynamicString("Algorithms Exam"),
                                            type = "Exam",
                                            dateTime = UiText.DynamicString("May 8 - 3:00 PM")
                                        )
                                    ),
                                    tasks = listOf(
                                        RoadmapTaskUiModel(
                                            title = UiText.DynamicString("Finish Graph Assignment"),
                                            dateTime = UiText.DynamicString("May 7 - 11:59 PM")
                                        ),
                                        RoadmapTaskUiModel(
                                            title = UiText.DynamicString("Review DFS/BFS"),
                                            dateTime = UiText.DynamicString("May 6 - 8:00 PM")
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                RoadmapWeekUiModel(
                    weekNumber = 2,
                    dateRange = UiText.DynamicString("May 13 - May 19"),
                    days = listOf(
                        RoadmapDayUiModel(
                            date = "2024-05-13",
                            dayName = UiText.DynamicString("Monday"),
                            blocks = listOf(
                                StudyBlockUiModel(
                                    id = "2",
                                    courseName = UiText.DynamicString("Database Systems"),
                                    topic = UiText.DynamicString("SQL Optimization"),
                                    durationMinutes = 90,
                                    isCompleted = true,
                                    color = StudyBlockColor.BLUE,
                                    tasks = listOf(
                                        RoadmapTaskUiModel(
                                            title = UiText.DynamicString("Practice Complex Joins"),
                                            dateTime = UiText.DynamicString("May 14 - 4:00 PM")
                                        )
                                    )
                                ),
                                StudyBlockUiModel(
                                    id = "3",
                                    courseName = UiText.DynamicString("Networks"),
                                    topic = UiText.DynamicString("OSI Model"),
                                    durationMinutes = 45,
                                    isCompleted = false,
                                    color = StudyBlockColor.GREEN,
                                    events = listOf(
                                        RoadmapEventUiModel(
                                            title = UiText.DynamicString("Networking Quiz"),
                                            type = "Quiz",
                                            dateTime = UiText.DynamicString("May 15 - 10:00 AM")
                                        )
                                    )
                                )
                            )
                        )
                    )
                ),
                RoadmapWeekUiModel(
                    weekNumber = 3,
                    dateRange = UiText.DynamicString("May 20 - May 26"),
                    days = listOf(
                        RoadmapDayUiModel(
                            date = "2024-05-20",
                            dayName = UiText.DynamicString("Monday"),
                            blocks = listOf(
                                StudyBlockUiModel(
                                    id = "4",
                                    courseName = UiText.DynamicString("Operating Systems"),
                                    topic = UiText.DynamicString("Memory Management"),
                                    durationMinutes = 120,
                                    isCompleted = false,
                                    color = StudyBlockColor.ORANGE
                                ),
                                StudyBlockUiModel(
                                    id = "5",
                                    courseName = UiText.DynamicString("Networks"),
                                    topic = UiText.DynamicString("TCP/UDP"),
                                    durationMinutes = 60,
                                    isCompleted = false,
                                    color = StudyBlockColor.GREEN
                                )
                            )
                        )
                    )
                )
            )

            fullRoadmapWeeks = mockWeeks
            _state.value = _state.value.copy(
                isLoading = false,
                roadmapStartDate = "2024-05-06",
                weeks = mockWeeks
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
