package com.iti.mongez.presentation.roadmap.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.res.stringResource
import com.iti.mongez.designsystem.components.button.AppButton
import com.iti.mongez.designsystem.components.button.AppButtonVariant
import com.iti.mongez.designsystem.components.chip.AppChip
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.roadmap.components.TimelineBlockItem
import com.iti.mongez.presentation.roadmap.components.WeekHeader
import com.iti.mongez.presentation.roadmap.contract.RoadmapEvent
import com.iti.mongez.presentation.roadmap.uiState.RoadmapDayUiModel
import com.iti.mongez.presentation.roadmap.uiState.RoadmapEventUiModel
import com.iti.mongez.presentation.roadmap.uiState.RoadmapUiState
import com.iti.mongez.presentation.roadmap.uiState.RoadmapWeekUiModel
import com.iti.mongez.presentation.roadmap.uiState.StudyBlockColor
import com.iti.mongez.presentation.roadmap.uiState.StudyBlockUiModel
import com.iti.mongez.presentation.roadmap.viewmodel.RoadmapViewModel
import com.iti.mongez.presentation.utils.UiText

@Composable
fun RoadmapScreen(
    innerPadding: PaddingValues,
    viewModel: RoadmapViewModel = hiltViewModel(),
    onNavigateToBlockDetails: (String) -> Unit,
    onNavigateToAddEvent: () -> Unit,
) {
    val state by viewModel.state.collectAsState()

    RoadmapContent(
        state = state,
        innerPadding = innerPadding,
        onEvent = viewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapContent(
    state: RoadmapUiState,
    innerPadding: PaddingValues,
    onEvent: (RoadmapEvent) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.roadmap_title),
                        style = Theme.typography.display.small,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Theme.colorScheme.surface.background
                )
            )
        },
        floatingActionButton = {
            AppButton(
                text = stringResource(R.string.roadmap_add_event),
                onClick = { onEvent(RoadmapEvent.OnAddEventClicked) },
                variant = AppButtonVariant.Primary,
                leadingIcon = Icons.Default.Add,
                fullWidth = false,
                modifier = Modifier.padding(bottom = Theme.spacing.lg)
            )
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colorScheme.surface.background)
                .padding(padding)
                .padding(horizontal = Theme.spacing.xl)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = Theme.spacing.md)
            ) {
                Text(
                    text = stringResource(
                        R.string.roadmap_filter_label,
                        state.filterStartDateDisplay,
                        state.filterEndDateDisplay
                    ),
                    style = Theme.typography.label.medium,
                    color = Theme.colorScheme.text.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = Theme.spacing.sm)
                )

                // The Range Slider
                RangeSlider(
                    value = state.dateRangeSliderValue,
                    onValueChange = { newRange ->
                        onEvent(RoadmapEvent.OnDateRangeChanged(newRange))
                    },
                    valueRange = 0f..100f,
                    steps = 0,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                state.weeks.forEach { week ->
                    item {
                        WeekHeader(week)
                    }
                    week.days.forEach { day ->
                        itemsIndexed(day.blocks) { blockIndex, block ->
                            val isLastBlock = blockIndex == day.blocks.size - 1 && 
                                            day == week.days.last() && 
                                            week == state.weeks.last()
                            
                            TimelineBlockItem(
                                block = block,
                                isLast = isLastBlock,
                                onClick = { onEvent(RoadmapEvent.OnBlockClicked(block.id)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoadmapScreenPreview() {
    val mockWeeks = listOf(
        RoadmapWeekUiModel(
            weekNumber = 1,
            dateRange = UiText.DynamicString("Mar 12 - Mar 18"),
            days = listOf(
                RoadmapDayUiModel(
                    date = "2024-03-12",
                    dayName = UiText.DynamicString("Tuesday"),
                    blocks = listOf(
                        StudyBlockUiModel(
                            id = "1",
                            courseName = UiText.DynamicString("Operating Systems"),
                            topic = UiText.DynamicString("Process Management"),
                            durationMinutes = 60,
                            isCompleted = true,
                            color = StudyBlockColor.PURPLE
                        ),
                        StudyBlockUiModel(
                            id = "2",
                            courseName = UiText.DynamicString("Algorithms"),
                            topic = UiText.DynamicString("Dynamic Programming"),
                            durationMinutes = 90,
                            isCompleted = false,
                            event = RoadmapEventUiModel(UiText.DynamicString("Quiz"), "Quiz"),
                            color = StudyBlockColor.PURPLE
                        )
                    )
                )
            )
        )
    )

    MongezTheme {
        RoadmapContent(
            state = RoadmapUiState(
                isLoading = false,
                weeks = mockWeeks,
                filterStartDateDisplay = "Mar 12",
                filterEndDateDisplay = "Mar 18"
            ),
            innerPadding = PaddingValues(0.dp),
            onEvent = {}
        )
    }
}
