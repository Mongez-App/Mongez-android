package com.iti.mongez.presentation.teamcourses.view


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.iti.mongez.domain.courses.model.Course
import com.iti.mongez.presentation.R
import com.iti.mongez.designsystem.components.tabs.AppPrimaryTabs
import com.iti.mongez.designsystem.components.textfield.AppTextField
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.presentation.courses.components.CoursesList
import com.iti.mongez.presentation.teamcourses.component.EmptyStateView
import com.iti.mongez.presentation.teamcourses.contract.TrackDetailsEffect
import com.iti.mongez.presentation.teamcourses.contract.TrackDetailsIntent
import com.iti.mongez.presentation.teamcourses.contract.TrackDetailsState
import com.iti.mongez.presentation.teamcourses.model.TrackEvent
import com.iti.mongez.presentation.teamcourses.viewmodel.TrackDetailsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.iti.mongez.designsystem.screens.dashboard.AppDeadlineCard
@Composable
fun TrackDetailsScreen(
    teamId: String,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: TrackDetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
    onNavigateToCourse: (String) -> Unit
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(teamId) {
        viewModel.onIntent(TrackDetailsIntent.LoadTeamDetails(teamId))
    }

    LaunchedEffect(key1 = true) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                TrackDetailsEffect.NavigateBack -> onNavigateBack()
                is TrackDetailsEffect.NavigateToCourse -> onNavigateToCourse(effect.courseId)
                is TrackDetailsEffect.ShowError -> coroutineScope.launch {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    TrackDetailsContent(
        state = state,
        onIntent = viewModel::onIntent,
        snackbarHostState = snackbarHostState
    )
}

@Composable
fun TrackDetailsContent(
    state: TrackDetailsState,
    onIntent: (TrackDetailsIntent) -> Unit,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }
) {
    Scaffold(
        containerColor = Theme.colorScheme.surface.background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TrackTopAppBar(
                title = stringResource(id = R.string.team_courses_title),
                onBackClick = { onIntent(TrackDetailsIntent.NavigateBack) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = Theme.spacing.lg)
        ) {
            Spacer(modifier = Modifier.height(Theme.spacing.md))

            // Reusable tabs from the Design System
            AppPrimaryTabs(
                tabs = listOf(
                    stringResource(id = R.string.courses),
                    stringResource(id = R.string.events)
                ),
                selectedTabIndex = state.selectedTabIndex,
                onTabSelected = { onIntent(TrackDetailsIntent.TabSelected(it)) }
            )

            Spacer(modifier = Modifier.height(Theme.spacing.lg))

            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Theme.colorScheme.brand.primary)
                }
            } else {
                when (state.selectedTabIndex) {
                    0 -> {
                        // Courses Tab
                        AppTextField(
                            value = state.searchQuery,
                            onValueChange = { onIntent(TrackDetailsIntent.SearchQueryChanged(it)) },
                            placeholder = stringResource(id = R.string.search_courses),
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (state.filteredCourses.isEmpty()) {
                            EmptyStateView(
                                iconRes = R.drawable.ic_empty_courses,
                                message = stringResource(id = R.string.no_courses_found)
                            )
                        } else {
                            CoursesList(
                                courses = state.filteredCourses,
                                onCourseClick = { onIntent(TrackDetailsIntent.CourseClicked(it)) },
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    1 -> {
                        // Events Tab
                        if (state.events.isEmpty()) {
                            EmptyStateView(
                                iconRes = R.drawable.ic_empty_events,
                                message = stringResource(id = R.string.no_events_found)
                            )
                        } else {
                            EventsGrid(
                                events = state.events,
                                onEventClick = { onIntent(TrackDetailsIntent.EventClicked(it)) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TrackTopAppBar(title: String, onBackClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = Theme.typography.headline.small,
                color = Theme.colorScheme.text.primary,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back),
                    tint = Theme.colorScheme.brand.primary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Theme.colorScheme.surface.background
        )
    )
}

@Composable
fun EventsGrid(
    events: List<TrackEvent>,
    onEventClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(Theme.spacing.md),
        verticalArrangement = Arrangement.spacedBy(Theme.spacing.md),
        contentPadding = PaddingValues(bottom = Theme.spacing.giant)
    ) {
        items(events, key = { it.id }) { event ->
            AppDeadlineCard(
                subject = event.courseName,
                taskType = event.eventName,
                timeLeft = event.timeRemaining,
                tintColor = event.tintColor(),
                modifier = Modifier.clickable { onEventClick(event.id) }
            )
        }
    }
}

@Composable
private fun TrackEvent.tintColor(): Color {
    return when (eventType.lowercase()) {
        "exam", "midterm", "quiz" -> Theme.colorScheme.state.error
        "assignment", "project" -> Theme.colorScheme.state.warning
        else -> Theme.colorScheme.state.info
    }
}

@Preview(showBackground = true)
@Composable
fun TrackDetailsPreview() {
    MongezTheme {
        val sampleCourses = listOf(
            Course(
                id = "course_1",
                name = "Electric Circuit",
                courseCode = "",
                imageUrl = null,
                startDate = "2026-08-19",
                examDate = "2026-08-31",
                hasMaterials = false,
                completionPercentage = 0f,
                isHidden = false,
                courseType = "STANDARD",
                materialUrl = null
            ),
            Course(
                id = "course_2",
                name = "Os",
                courseCode = "",
                imageUrl = null,
                startDate = "2026-08-17",
                examDate = "2026-09-16",
                hasMaterials = false,
                completionPercentage = 0f,
                isHidden = false,
                courseType = "STANDARD",
                materialUrl = null
            )
        )
        val sampleEvents = listOf(
            TrackEvent(
                id = "1",
                courseName = "Electric Circuit",
                eventName = "Assignment",
                timeRemaining = "Tomorrow",
                eventType = "assignment"
            )
        )
        TrackDetailsContent(
            state = TrackDetailsState(
                courses = sampleCourses,
                filteredCourses = sampleCourses,
                events = sampleEvents
            ),
            onIntent = {}
        )
    }
}
