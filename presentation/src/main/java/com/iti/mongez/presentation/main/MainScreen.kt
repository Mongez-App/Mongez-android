package com.iti.mongez.presentation.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.iti.mongez.designsystem.components.navigation.AppNavigationBar
import com.iti.mongez.designsystem.components.navigation.AppNavigationBarItem
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarContent
import com.iti.mongez.designsystem.components.snackbar.AppSnackbarType
import com.iti.mongez.designsystem.theme.Theme
import com.iti.mongez.presentation.courses.CoursesScreen
import com.iti.mongez.presentation.dashboard.DashboardScreen
import com.iti.mongez.presentation.preferences.components.DefaultScheduleDialog

private val navigationItems = listOf(
    AppNavigationBarItem(
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
    ),
    AppNavigationBarItem(
        label = "Courses",
        selectedIcon = Icons.AutoMirrored.Filled.MenuBook,
        unselectedIcon = Icons.AutoMirrored.Outlined.MenuBook,
    ),
    AppNavigationBarItem(
        label = "Roadmap",
        selectedIcon = Icons.Filled.CalendarMonth,
        unselectedIcon = Icons.Outlined.CalendarMonth,
    ),
    AppNavigationBarItem(
        label = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
    ),
)

@Composable
fun MainScreen(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    showDefaultAlert: Boolean = false
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    var activeSnackbarType by remember { mutableStateOf(AppSnackbarType.Info) }
    var isDefaultScheduleDialogOpen by remember { mutableStateOf(showDefaultAlert) }

    if (isDefaultScheduleDialogOpen) {
        DefaultScheduleDialog(
            onDismiss = { isDefaultScheduleDialogOpen = false },
            onGoToSettings = {
                isDefaultScheduleDialogOpen = false
                // Logic to go to settings
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Theme.colorScheme.surface.background,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                AppSnackbarContent(
                    message = snackbarData.visuals.message,
                    type = activeSnackbarType,
                    modifier = Modifier.padding(Theme.spacing.md)
                )
            }
        },
        bottomBar = {
            AppNavigationBar(
                items = navigationItems,
                selectedIndex = selectedTab,
                onItemSelected = { selectedTab = it },
            )
        },
    ) { innerPadding ->
        when (selectedTab) {
            0 -> {
                DashboardScreen(
                    innerPadding = innerPadding,
                    onNavigateToFocus = {},
                    onViewAllTasks = {},
                    onViewAllDeadlines = {},
                )
            }
            1 -> {
                CoursesScreen(
                    innerPadding = innerPadding,
                    viewModel = hiltViewModel()
                )
            }
            2 -> Text("Roadmap Content", modifier = Modifier.padding(innerPadding))
            3 -> Text("Profile Content", modifier = Modifier.padding(innerPadding))
        }
    }
}
