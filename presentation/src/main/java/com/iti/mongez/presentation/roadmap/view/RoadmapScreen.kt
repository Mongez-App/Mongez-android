package com.iti.mongez.presentation.roadmap.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.res.stringResource
import java.time.LocalDate
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.designsystem.components.common.AppEmptyState
import com.iti.mongez.designsystem.components.loading.AppCircularLoading
import com.iti.mongez.designsystem.components.dialog.AppConfirmationDialog
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.roadmap.components.ActiveFiltersRow
import com.iti.mongez.presentation.roadmap.components.AddEventDialog
import com.iti.mongez.presentation.roadmap.components.FilterBottomSheet
import com.iti.mongez.presentation.roadmap.components.TimelineBlockItem
import com.iti.mongez.presentation.roadmap.components.WeekHeader
import com.iti.mongez.presentation.roadmap.contract.RoadmapEffect
import com.iti.mongez.presentation.roadmap.contract.RoadmapEvent
import com.iti.mongez.presentation.roadmap.uiState.RoadmapFilterState
import com.iti.mongez.presentation.roadmap.uiState.RoadmapUiState
import com.iti.mongez.presentation.roadmap.uiState.RoadmapWeekUiModel
import com.iti.mongez.presentation.roadmap.uiState.StudyBlockColor
import com.iti.mongez.presentation.roadmap.uiState.StudyBlockUiModel
import com.iti.mongez.presentation.roadmap.viewmodel.RoadmapViewModel
import com.iti.mongez.presentation.utils.UiText

private fun Modifier.roadmapActionShadow(
    shadowColor: Color,
    backgroundColor: Color
): Modifier = this.drawBehind {
    val shadowColorArgb = shadowColor.copy(alpha = 0.7f).toArgb()
    
    drawIntoCanvas { canvas ->
        val paint = android.graphics.Paint()
        paint.color = backgroundColor.toArgb()
        
        val blurRadius = 5.dp.toPx()

        paint.setShadowLayer(
            blurRadius,
            0f,
            0f,
            shadowColorArgb
        )
        
        canvas.nativeCanvas.drawCircle(
            size.width / 2,
            size.height / 2,
            size.width / 2,
            paint
        )
    }
}

@Composable
fun RoadmapScreen(
    innerPadding: PaddingValues,
    viewModel: RoadmapViewModel = hiltViewModel(),
    onNavigateToCourses: () -> Unit,
    onShowSnackBar: (String, AppSnackbarType?) -> Unit = { _, _ -> }
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(RoadmapEvent.LoadRoadmap())
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is RoadmapEffect.ShowNoCoursesDialog -> {
                    viewModel.onEvent(RoadmapEvent.ToggleNoCoursesDialog(true))
                }
                is RoadmapEffect.ShowSnackBar -> {
                    onShowSnackBar(effect.message, effect.type)
                }
                else -> {}
            }
        }
    }

    RoadmapContent(
        state = state,
        innerPadding = innerPadding,
        onEvent = viewModel::onEvent
    )

    if (state.isAddEventDialogVisible) {
        AddEventDialog(
            availableCourses = state.availableCourses,
            onDismiss = { viewModel.onEvent(RoadmapEvent.ToggleAddEventDialog(false)) },
            onEventCreated = { type, courseId, name, date, time ->
                viewModel.onEvent(
                    RoadmapEvent.AddEvent(
                        type = type,
                        course = courseId,
                        name = name,
                        date = date,
                        time = time
                    )
                )
            }
        )
    }

    if (state.isFilterSheetVisible) {
        FilterBottomSheet(
            initialState = state.activeFilterState,
            availableCourses = state.availableCourses,
            availableEventTypes = state.availableEventTypes,
            onDismiss = { viewModel.onEvent(RoadmapEvent.ToggleFilterSheet(false)) },
            onApply = { 
                viewModel.onEvent(RoadmapEvent.ApplyFilter(it))
            }
        )
    }

    if (state.isNoCoursesDialogVisible) {
        AppConfirmationDialog(
            title = stringResource(R.string.no_courses_dialog_title),
            description = stringResource(R.string.no_courses_dialog_description),
            primaryActionText = stringResource(R.string.button_add_course),
            onPrimaryAction = {
                viewModel.onEvent(RoadmapEvent.ToggleNoCoursesDialog(false))
                onNavigateToCourses()
            },
            onDismiss = { viewModel.onEvent(RoadmapEvent.ToggleNoCoursesDialog(false)) },
            secondaryActionText = stringResource(R.string.action_cancel),
            onSecondaryAction = { viewModel.onEvent(RoadmapEvent.ToggleNoCoursesDialog(false)) },
            isHorizontal = true
        )
    }
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
                        style = Theme.typography.headline.medium,
                        color = Theme.colorScheme.text.primary
                    )
                },
                actions = {
                    IconButton(
                        onClick = { onEvent(RoadmapEvent.ToggleFilterSheet(true)) },
                        modifier = Modifier
                            .padding(end = Theme.spacing.lg)
                            .size(Theme.spacing.xxxl)
                            .roadmapActionShadow(
                                shadowColor = Theme.colorScheme.brand.primary,
                                backgroundColor = Theme.colorScheme.surface.background
                            )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.filter),
                            contentDescription = null,
                            tint = Theme.colorScheme.brand.primary
                        )
                    }


                    IconButton(
                        onClick = { onEvent(RoadmapEvent.OnAddEventClicked) },
                        modifier = Modifier
                            .padding(end = Theme.spacing.lg)
                            .size(Theme.spacing.xxxl)
                            .roadmapActionShadow(
                                shadowColor = Theme.colorScheme.brand.primary,
                                backgroundColor = Theme.colorScheme.surface.background
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Theme.colorScheme.brand.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Theme.colorScheme.surface.background
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colorScheme.surface.background)
                .padding(padding)
                .padding(horizontal = Theme.spacing.lg)
        ) {

            if (state.activeFilterState.hasActiveFilters) {
                ActiveFiltersRow(
                    filterState = state.activeFilterState,
                    onEvent = onEvent,
                modifier = Modifier.padding(start = Theme.spacing.lg, end = Theme.spacing.lg, bottom = Theme.spacing.sm)
                )
            }

            if (state.isLoading && state.weeks.isEmpty()) {
                AppCircularLoading()
            } else if (state.weeks.isEmpty()) {
                AppEmptyState(
                    title = stringResource(id = R.string.roadmap_empty_state_title),
                    description = stringResource(id = R.string.roadmap_empty_state_desc),
                    actionText = stringResource(id = R.string.roadmap_add_event),
                    onAction = { onEvent(RoadmapEvent.OnAddEventClicked) }
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = Theme.spacing.xxl)
                ) {
                    state.weeks.forEach { week ->
                        item {
                            WeekHeader(week)
                        }
                        itemsIndexed(
                            items = week.blocks,
                            key = { _, block -> "${week.weekNumber}_${block.id}" }
                        ) { _, block ->
                            TimelineBlockItem(
                                block = block
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
            dateRange = UiText.DynamicString("May 6 - May 12"),
            blocks = listOf(
                StudyBlockUiModel(
                    id = "1",
                    courseName = UiText.DynamicString("Algorithms"),
                    topic = UiText.DynamicString("Graph Theory"),
                    durationMinutes = 60,
                    isCompleted = true,
                    color = StudyBlockColor.PURPLE
                )
            )
        ),
        RoadmapWeekUiModel(
            weekNumber = 2,
            dateRange = UiText.DynamicString("May 13 - May 19"),
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

    MongezTheme {
        RoadmapContent(
            state = RoadmapUiState(
                isLoading = false,
                weeks = mockWeeks,
                activeFilterState = RoadmapFilterState(
                    startDate = LocalDate.of(2024, 5, 6),
                    endDate = LocalDate.of(2024, 5, 30)
                )
            ),
            innerPadding = PaddingValues(0.dp),
            onEvent = {}
        )
    }
}
