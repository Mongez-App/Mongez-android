package com.iti.mongez.presentation.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import com.iti.mongez.designsystem.components.navigation.AppNavigationBar
import com.iti.mongez.designsystem.components.navigation.AppNavigationBarItem
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.courses.view.CoursesScreen
import com.iti.mongez.presentation.dashboard.DashboardScreen
import com.iti.mongez.presentation.dashboard.TaskItem
import com.iti.mongez.presentation.preferences.components.DefaultScheduleDialog
import com.iti.mongez.presentation.profile.view.ProfileScreen
import com.iti.mongez.presentation.roadmap.view.RoadmapScreen
import com.iti.mongez.presentation.teamcourses.view.TrackDetailsScreen

/**
 * Represents the tabs available in the main bottom navigation.
 */


@Composable
fun MainScreen(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    showDefaultAlert: Boolean = false,
    viewModel: MainViewModel = hiltViewModel(),
    onNavigateToPreferences: () -> Unit,
    onNavigateToCourseDetails: (String) -> Unit,
    onNavigateToReadOnlyCourseDetails: (String) -> Unit = onNavigateToCourseDetails,
    onNavigateToStudyRoom: (String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToAllTasks: (List<TaskItem>) -> Unit
) {
    val preferences by viewModel.preferences.collectAsState()
    
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val selectedTab = MainTab.fromIndex(selectedTabIndex)

    var activeSnackbarType by remember {
        mutableStateOf(AppSnackbarType.Info)
    }

    val coroutineScope = rememberCoroutineScope()

    var isDefaultScheduleDialogOpen by rememberSaveable {
        mutableStateOf(showDefaultAlert)
    }

    BackHandler(enabled = selectedTabIndex != 0) {
        selectedTabIndex = 0
    }

    if (isDefaultScheduleDialogOpen) {
        preferences?.let { prefs ->
            DefaultScheduleDialog(
                studyHours = prefs.dailyStudyHours,
                selectedDays = prefs.availableDays,
                onDismiss = {
                    isDefaultScheduleDialogOpen = false
                },
                onGoToProfile = {
                    isDefaultScheduleDialogOpen = false
                    selectedTabIndex = 3
                }
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Theme.colorScheme.surface.background,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            ) { snackbarData ->
                AppSnackbarContent(
                    message = snackbarData.visuals.message,
                    type = activeSnackbarType,
                    modifier = Modifier.padding(Theme.spacing.md)
                )
            }
        },
        bottomBar = {
            AppNavigationBar(
                items = MainTab.entries.map { tab ->
                    AppNavigationBarItem(
                        label = stringResource(tab.labelResId),
                        selectedIcon = tab.selectedIcon,
                        unselectedIcon = tab.unselectedIcon
                    )
                },
                selectedIndex = selectedTabIndex,
                onItemSelected = {
                    selectedTabIndex = it
                }
            )
        }
    ) { innerPadding ->

        MainScreenContent(
            tab = selectedTab,
            innerPadding = innerPadding,
            onNavigateToPreferences = onNavigateToPreferences,
            onNavigateToCourseDetails = onNavigateToCourseDetails,
            onNavigateToReadOnlyCourseDetails = onNavigateToReadOnlyCourseDetails,
            onNavigateToStudyRoom = onNavigateToStudyRoom,
            onNavigateToLogin = onNavigateToLogin,
            onNavigateToAllTasks = onNavigateToAllTasks,
            onNavigateToCourses = { selectedTabIndex = 1 },
            onShowSnackBar = { message, type ->
                activeSnackbarType = type ?: AppSnackbarType.Info
                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            }
        )
    }
}

@Composable
private fun MainScreenContent(
    tab: MainTab,
    innerPadding: PaddingValues,
    onNavigateToPreferences: () -> Unit,
    onNavigateToCourseDetails: (String) -> Unit,
    onNavigateToReadOnlyCourseDetails: (String) -> Unit,
    onNavigateToStudyRoom: (String, String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToAllTasks: (List<TaskItem>) -> Unit,
    onNavigateToCourses: () -> Unit,
    onShowSnackBar: (String, AppSnackbarType?) -> Unit,
) {
    when (tab) {
        MainTab.Home -> {
            DashboardScreen(
                innerPadding = innerPadding,
                onNavigateToFocus = {},
                onViewAllDeadlines = {},
                onNavigateToStudyRoom = onNavigateToStudyRoom,
                onViewAllTasks = { tasks ->
                    onNavigateToAllTasks(tasks)
                }
            )
        }

        MainTab.Courses -> {
            CoursesScreen(
                innerPadding = innerPadding,
                viewModel = hiltViewModel(),
                onCourseClick = onNavigateToCourseDetails
            )
        }

        MainTab.Roadmap -> {
            RoadmapScreen(
                innerPadding = innerPadding,
                onNavigateToCourses = onNavigateToCourses,
                onShowSnackBar = onShowSnackBar
            )
        }
        MainTab.Track -> {
            TrackDetailsScreen(
                onNavigateBack = { /* Optional: switch tab or handle back */ },
                onNavigateToCourse = onNavigateToReadOnlyCourseDetails
            )
        }

        MainTab.Profile -> {
            ProfileScreen(
                innerPadding = innerPadding,
                viewModel = hiltViewModel(),
                onNavigateToLogin = onNavigateToLogin,
                onShowSnackBar = { message -> onShowSnackBar(message, null) }
            )
        }
    }
}
