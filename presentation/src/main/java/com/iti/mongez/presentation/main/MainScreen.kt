package com.iti.mongez.presentation.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.designsystem.components.navigation.AppNavigationBar
import com.iti.mongez.designsystem.components.navigation.AppNavigationBarItem
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.R
import com.iti.mongez.presentation.courses.view.CoursesScreen
import com.iti.mongez.presentation.dashboard.DashboardScreen
import com.iti.mongez.presentation.preferences.components.DefaultScheduleDialog

/**
 * Represents the tabs available in the main bottom navigation.
 */


@Composable
fun MainScreen(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    showDefaultAlert: Boolean = false,
    onNavigateToCourseDetails: (String) -> Unit
) {
    var selectedTabIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val selectedTab = MainTab.fromIndex(selectedTabIndex)

    var activeSnackbarType by remember {
        mutableStateOf(AppSnackbarType.Info)
    }

    var isDefaultScheduleDialogOpen by rememberSaveable {
        mutableStateOf(showDefaultAlert)
    }

    if (isDefaultScheduleDialogOpen) {
        DefaultScheduleDialog(
            onDismiss = {
                isDefaultScheduleDialogOpen = false
            },
            onGoToSettings = {
                isDefaultScheduleDialogOpen = false
                // TODO: Navigate to Settings
            }
        )
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
            onNavigateToCourseDetails = onNavigateToCourseDetails
        )
    }
}

@Composable
private fun MainScreenContent(
    tab: MainTab,
    innerPadding: PaddingValues,
    onNavigateToCourseDetails: (String) -> Unit
) {
    when (tab) {

        MainTab.Home -> {
            DashboardScreen(
                innerPadding = innerPadding,
                onNavigateToFocus = {},
                onViewAllTasks = {},
                onViewAllDeadlines = {}
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
            Text(
                text = stringResource(R.string.nav_roadmap) + " Content",
                modifier = Modifier.padding(innerPadding),
                color = Theme.colorScheme.text.primary,
                style = Theme.typography.title.medium
            )
        }

        MainTab.Profile -> {
            Text(
                text = stringResource(R.string.nav_profile) + " Content",
                modifier = Modifier.padding(innerPadding),
                color = Theme.colorScheme.text.primary,
                style = Theme.typography.title.medium
            )
        }
    }
}