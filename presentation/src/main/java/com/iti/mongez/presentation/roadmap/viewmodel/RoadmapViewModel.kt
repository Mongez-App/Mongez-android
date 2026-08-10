package com.iti.mongez.presentation.roadmap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.domain.roadmap.usecase.GetWeeklyRoadmapUseCase
import com.iti.mongez.domain.courses.usecase.GetCoursesUseCase
import com.iti.mongez.presentation.roadmap.contract.RoadmapEffect
import com.iti.mongez.presentation.roadmap.contract.RoadmapEvent
import com.iti.mongez.presentation.roadmap.uiState.RoadmapEventUiModel
import com.iti.mongez.presentation.roadmap.uiState.RoadmapTaskUiModel
import com.iti.mongez.presentation.roadmap.uiState.RoadmapUiState
import com.iti.mongez.presentation.roadmap.uiState.RoadmapWeekUiModel
import com.iti.mongez.presentation.roadmap.uiState.StudyBlockUiModel
import com.iti.mongez.presentation.roadmap.uiState.StudyBlockColor
import com.iti.mongez.presentation.utils.UiText
import com.iti.mongez.presentation.roadmap.uiState.RoadmapFilterState
import com.iti.mongez.presentation.roadmap.uiState.CourseUiModel
import com.iti.mongez.domain.roadmap.usecase.AddEventUseCase
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
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.time.Instant
import java.time.ZoneId

@HiltViewModel
class RoadmapViewModel @Inject constructor(
    private val getWeeklyRoadmapUseCase: GetWeeklyRoadmapUseCase,
    private val addEventUseCase: AddEventUseCase,
    private val getCoursesUseCase: GetCoursesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RoadmapUiState(isLoading = true))
    val state: StateFlow<RoadmapUiState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<RoadmapEffect>()
    val effect: SharedFlow<RoadmapEffect> = _effect.asSharedFlow()

    private var fullRoadmapWeeks: List<RoadmapWeekUiModel> = emptyList()
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())

    init {
        fetchCourses()
        loadRoadmap()
    }

    private fun fetchCourses() {
        viewModelScope.launch {
            getCoursesUseCase().fold(
                onSuccess = { courses ->
                    _state.value = _state.value.copy(
                        availableCourses = courses.map { CourseUiModel(it.id, it.name) }
                    )
                },
                onFailure = { /* Ignore and rely on loadRoadmap courses if needed */ },
                onLoading = {}
            )
        }
    }

    private fun loadRoadmap(startDate: String? = null, forceDefault: Boolean = false) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            
            val fetchDate = if (forceDefault) null else (startDate ?: _state.value.roadmapStartDate.ifEmpty { null })

            getWeeklyRoadmapUseCase(fetchDate).fold(
                onSuccess = { roadmap ->
                    val courseColors = mutableMapOf<String, StudyBlockColor>()
                    val colors = StudyBlockColor.entries.toTypedArray()
                    var colorIndex = 0

                    val uiWeeks = roadmap.weeks.map { week ->
                        val weekStartDate = LocalDate.parse(week.startDate)
                        val weekEndDate = LocalDate.parse(week.endDate)
                        val uiBlocks = week.studyBlocks
                            .groupBy { it.courseId }
                            .map { (_, blocks) ->
                                val firstBlock = blocks.first()
                                
                                val allTasks = blocks.flatMap { it.tasks }.filter { task ->
                                    val taskDate = LocalDate.parse(task.taskDate)
                                    !taskDate.isBefore(weekStartDate) && !taskDate.isAfter(weekEndDate)
                                }
                                val allEvents = blocks.flatMap { it.events }.filter { event ->
                                    val eventDate = LocalDate.parse(event.eventDate.substringBefore('T'))
                                    !eventDate.isBefore(weekStartDate) && !eventDate.isAfter(weekEndDate)
                                }

                                StudyBlockUiModel(
                                    id = firstBlock.id,
                                    courseName = UiText.DynamicString(firstBlock.courseName),
                                    topic = UiText.DynamicString(allTasks.firstOrNull()?.topic ?: firstBlock.courseName),
                                    durationMinutes = allTasks.sumOf { it.durationMinutes },
                                    isCompleted = blocks.any { it.isCompleted },
                                    color = courseColors.getOrPut(firstBlock.courseName) {
                                        val selectedColor = colors[colorIndex % colors.size]
                                        colorIndex++
                                        selectedColor
                                    },
                                    tasks = allTasks.map { task ->
                                        val date = LocalDate.parse(task.taskDate)
                                        val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                                        RoadmapTaskUiModel(
                                            title = UiText.DynamicString(task.topic),
                                            dateTime = UiText.DynamicString("$dayName, ${task.durationMinutes} min"),
                                            date = date
                                        )
                                    },
                                    events = allEvents.map { event ->
                                        val dateTime = try {
                                            Instant.parse(event.eventDate).atZone(ZoneId.systemDefault()).toLocalDateTime()
                                        } catch (e: Exception) {
                                            LocalDate.parse(event.eventDate.substringBefore('T')).atStartOfDay()
                                        }
                                        val dayName = dateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                                        val time = dateTime.format(DateTimeFormatter.ofPattern("hh:mm a"))

                                        RoadmapEventUiModel(
                                            title = UiText.DynamicString(event.title),
                                            type = event.eventType,
                                            dateTime = UiText.DynamicString("$dayName, $time"),
                                            date = dateTime.toLocalDate()
                                        )
                                    }
                                )
                            }

                        val rangeFormatter = DateTimeFormatter.ofPattern("MMM d")
                        val dateRange = "${weekStartDate.format(rangeFormatter)} - ${weekEndDate.format(rangeFormatter)}"

                        RoadmapWeekUiModel(
                            weekNumber = week.weekNumber,
                            dateRange = UiText.DynamicString(dateRange),
                            blocks = uiBlocks
                        )
                    }

                    val allEventTypes = uiWeeks.flatMap { week ->
                        week.blocks.flatMap { it.events.map { event -> event.type } }
                    }.distinct().sorted().ifEmpty { listOf("Study", "Assignment", "Quiz", "Exam", "Reminder") }

                    fullRoadmapWeeks = uiWeeks
                    _state.value = _state.value.copy(
                        isLoading = false,
                        roadmapStartDate = roadmap.roadmapStartDate,
                        weeks = uiWeeks,
                        availableEventTypes = allEventTypes
                    )
                    applyFilters(_state.value.activeFilterState)
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
                is RoadmapEvent.LoadRoadmap -> loadRoadmap(event.startDate)
                is RoadmapEvent.OnAddEventClicked -> {
                    if (_state.value.availableCourses.isEmpty()) {
                        _effect.emit(RoadmapEffect.ShowNoCoursesDialog)
                    } else {
                        _state.value = _state.value.copy(isAddEventDialogVisible = true)
                    }
                }
                is RoadmapEvent.ToggleFilterSheet -> {
                    if (event.isVisible && _state.value.availableCourses.isEmpty()) {
                        _effect.emit(RoadmapEffect.ShowNoCoursesDialog)
                    } else {
                        _state.value = _state.value.copy(isFilterSheetVisible = event.isVisible)
                    }
                }
                is RoadmapEvent.ToggleAddEventDialog -> {
                    _state.value = _state.value.copy(isAddEventDialogVisible = event.isVisible)
                }
                is RoadmapEvent.ToggleNoCoursesDialog -> {
                    _state.value = _state.value.copy(isNoCoursesDialogVisible = event.isVisible)
                }
                is RoadmapEvent.ApplyFilter -> {
                    val filterState = event.filterState
                    val currentStartDate = _state.value.roadmapStartDate

                    if (filterState.startDate != null && currentStartDate.isNotEmpty()) {
                        try {
                            val selectedDate = filterState.startDate
                            val roadmapDate = LocalDate.parse(currentStartDate)

                            if (selectedDate.isBefore(roadmapDate)) {
                                _state.value = _state.value.copy(
                                    isFilterSheetVisible = false,
                                    activeFilterState = filterState
                                )
                                loadRoadmap(selectedDate.toString())
                                return@launch
                            }
                        } catch (e: Exception) {
                            android.util.Log.e("RoadmapViewModel", "Error parsing roadmap date", e)
                        }
                    }

                    _state.value = _state.value.copy(
                        isFilterSheetVisible = false,
                        activeFilterState = filterState
                    )
                    applyFilters(filterState)
                }
                is RoadmapEvent.ClearAllFilters -> {
                    val clearedState = RoadmapFilterState()
                    _state.value = _state.value.copy(activeFilterState = clearedState)
                    loadRoadmap(forceDefault = true)
                }
                is RoadmapEvent.RemoveDateFilter -> {
                    val newState = _state.value.activeFilterState.copy(startDate = null, endDate = null)
                    _state.value = _state.value.copy(activeFilterState = newState)
                    loadRoadmap(forceDefault = true)
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
                    _state.value = _state.value.copy(isAddEventDialogVisible = false)
                    _state.value = _state.value.copy(isLoading = true)

                    // Format date: "MMM dd, yyyy" -> "yyyy-MM-dd"
                    val inputFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.getDefault())
                    val outputDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val date = LocalDate.parse(event.date, inputFormatter).format(outputDateFormatter)

                    // Format time: "hh:mm a" -> "HH:mm:00"
                    val inputTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
                    val time = LocalTime.parse(event.time, inputTimeFormatter).format(DateTimeFormatter.ofPattern("HH:mm:ss"))

                    val isoDateTime = "${date}T${time}Z"

                    addEventUseCase(
                        courseId = event.course,
                        title = event.name,
                        eventType = event.type.uppercase(),
                        eventDate = isoDateTime
                    ).fold(
                        onSuccess = { message ->
                            _effect.emit(
                                RoadmapEffect.ShowSnackBar(
                                    message = message,
                                    type = AppSnackbarType.Success
                                )
                            )
                            loadRoadmap()
                        },
                        onFailure = { error ->
                            _state.value = _state.value.copy(isLoading = false)
                            _effect.emit(
                                RoadmapEffect.ShowSnackBar(
                                    message = error.message ?: "Failed to add event",
                                    type = AppSnackbarType.Error
                                )
                            )
                        },
                        onLoading = {}
                    )
                }
            }
        }
    }

    private fun applyFilters(filterState: RoadmapFilterState) {
        val filteredWeeks = fullRoadmapWeeks.mapNotNull { week ->
            val filteredBlocks = week.blocks.mapNotNull { block ->
                // 1. Course Filter
                val matchesCourse = filterState.selectedCourses.isEmpty() ||
                        filterState.selectedCourses.contains(block.courseName.asRawString())

                if (!matchesCourse) return@mapNotNull null

                // 2. Date Filter
                val filteredTasks = block.tasks.filter { task ->
                    if (filterState.startDate != null && filterState.endDate != null) {
                        val taskDate = task.date ?: return@filter false
                        !taskDate.isBefore(filterState.startDate) && !taskDate.isAfter(filterState.endDate)
                    } else true
                }

                val filteredEvents = block.events.filter { event ->
                    val matchesDate = if (filterState.startDate != null && filterState.endDate != null) {
                        val eventDate = event.date ?: return@filter false
                        !eventDate.isBefore(filterState.startDate) && !eventDate.isAfter(filterState.endDate)
                    } else true

                    val matchesType = filterState.selectedEventTypes.isEmpty() ||
                            filterState.selectedEventTypes.any { it.equals(event.type, ignoreCase = true) }

                    matchesDate && matchesType
                }

                val hasMatchingEvents = filteredEvents.isNotEmpty()
                val hasMatchingTasks = filteredTasks.isNotEmpty()

                val shouldShowBlock = if (filterState.selectedEventTypes.isNotEmpty()) {
                    hasMatchingEvents
                } else {
                    hasMatchingEvents || hasMatchingTasks
                }

                if (shouldShowBlock || !filterState.hasActiveFilters) {
                    block.copy(
                        tasks = if (filterState.selectedEventTypes.isNotEmpty()) emptyList() else filteredTasks,
                        events = filteredEvents
                    )
                } else {
                    null
                }
            }

            if (filteredBlocks.isNotEmpty()) {
                week.copy(blocks = filteredBlocks)
            } else {
                null
            }
        }

        _state.value = _state.value.copy(weeks = filteredWeeks)
    }
}
