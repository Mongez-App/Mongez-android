package com.iti.mongez.presentation.roadmap.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.domain.roadmap.usecase.GetWeeklyRoadmapUseCase
import com.iti.mongez.presentation.roadmap.contract.RoadmapEffect
import com.iti.mongez.presentation.roadmap.contract.RoadmapEvent
import com.iti.mongez.presentation.roadmap.uiState.RoadmapDayUiModel
import com.iti.mongez.presentation.roadmap.uiState.RoadmapEventUiModel
import com.iti.mongez.presentation.roadmap.uiState.RoadmapUiState
import com.iti.mongez.presentation.roadmap.uiState.RoadmapWeekUiModel
import com.iti.mongez.presentation.roadmap.uiState.StudyBlockUiModel
import com.iti.mongez.presentation.roadmap.uiState.StudyBlockColor
import com.iti.mongez.presentation.utils.UiText
import com.iti.mongez.presentation.R
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
                                    event = RoadmapEventUiModel(
                                        title = UiText.DynamicString("Algorithms Exam"),
                                        type = "Exam",
                                        dateTime = UiText.DynamicString("May 8 - 3:00 PM")
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
                                    color = StudyBlockColor.BLUE
                                ),
                                StudyBlockUiModel(
                                    id = "3",
                                    courseName = UiText.DynamicString("Networks"),
                                    topic = UiText.DynamicString("OSI Model"),
                                    durationMinutes = 45,
                                    isCompleted = false,
                                    color = StudyBlockColor.GREEN
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
                    _effect.emit(RoadmapEffect.NavigateToAddEvent)
                }
                is RoadmapEvent.OnDateRangeChanged -> {
                    updateDateRangeFilter(event.range)
                }
            }
        }
    }

    private fun updateDateRangeFilter(range: ClosedFloatingPointRange<Float>) {
        val startDate = LocalDate.parse(_state.value.roadmapStartDate, dateFormatter)
        val totalDays = (fullRoadmapWeeks.size * 7).toLong()

        val startOffset = (range.start / 100f * totalDays).toLong()
        val endOffset = (range.endInclusive / 100f * totalDays).toLong()

        val filteredStartDate = startDate.plusDays(startOffset)
        val filteredEndDate = startDate.plusDays(endOffset)

        val displayFormatter = DateTimeFormatter.ofPattern("MMM dd", Locale.getDefault())

        _state.value = _state.value.copy(
            dateRangeSliderValue = range,
            filterStartDateDisplay = filteredStartDate.format(displayFormatter),
            filterEndDateDisplay = filteredEndDate.format(displayFormatter)
        )

        filterRoadmapData(filteredStartDate, filteredEndDate)
    }

    private fun filterRoadmapData(start: LocalDate, end: LocalDate) {
        val filteredWeeks = fullRoadmapWeeks.mapNotNull { week ->
            val filteredDays = week.days.filter { day ->
                val dayDate = LocalDate.parse(day.date, dateFormatter)
                !dayDate.isBefore(start) && !dayDate.isAfter(end)
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
