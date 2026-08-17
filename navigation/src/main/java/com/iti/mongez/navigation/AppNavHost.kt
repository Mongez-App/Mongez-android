package com.iti.mongez.navigation


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavEntry
import com.iti.mongez.domain.core.usecase.GetInitialRouteUseCase
import com.iti.mongez.domain.utils.StartDestination
import com.iti.mongez.presentation.coursedetails.view.CourseDetailsScreen
import com.iti.mongez.presentation.auth.login.view.LoginScreen
import com.iti.mongez.presentation.auth.register.view.RegisterScreen
import com.iti.mongez.presentation.main.MainScreen
import com.iti.mongez.presentation.onboarding.view.OnboardingScreen
import androidx.compose.foundation.layout.PaddingValues
import com.iti.mongez.presentation.preferences.view.PreferencesScreen
import com.iti.mongez.presentation.profile.view.ProfileScreen
import com.iti.mongez.presentation.roadmap.view.RoadmapScreen
import com.iti.mongez.presentation.studyroom.view.StudyRoomScreen
import com.iti.mongez.domain.settings.model.AppSettings
import com.iti.mongez.domain.settings.usecase.GetAppSettingsUseCase
import com.iti.mongez.presentation.dashboard.AllTasksScreen
import com.iti.mongez.presentation.teamcourses.view.TrackDetailsScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val getInitialRouteUseCase: GetInitialRouteUseCase,
    private val getAppSettingsUseCase: GetAppSettingsUseCase
) : ViewModel() {

    private val _startDestination = MutableStateFlow<AppRoute?>(null)
    val startDestination: StateFlow<AppRoute?> = _startDestination.asStateFlow()

    private val _appSettings = MutableStateFlow(AppSettings())
    val appSettings: StateFlow<AppSettings> = _appSettings.asStateFlow()

    init {
        viewModelScope.launch {
            val destination = getInitialRouteUseCase()
            _startDestination.value = when (destination) {
                StartDestination.ONBOARDING -> AppRoute.Onboarding
                StartDestination.LOGIN -> AppRoute.Login
                StartDestination.PREFERENCES -> AppRoute.Preferences
                StartDestination.DASHBOARD -> AppRoute.Dashboard()
            }
        }
        
        getAppSettingsUseCase()
            .onEach { settings ->
                _appSettings.value = settings
            }.launchIn(viewModelScope)
    }
}

@Composable
fun AppNavHost(viewModel: NavViewModel = hiltViewModel()) {
    // The splash screen is held until startDestination is resolved,
    // so it is guaranteed non-null by the time this composable renders.
    val startDestination by viewModel.startDestination.collectAsState()
    val resolvedDestination = startDestination ?: return

    val backStack = remember { mutableStateListOf(resolvedDestination) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() }
    ) { key ->
        when (key) {
            is AppRoute.Onboarding -> NavEntry(AppRoute.Onboarding) {
                OnboardingScreen(
                    viewModel = hiltViewModel(),
                    onNavigateToAuth = {
                        backStack.clear()
                        backStack.add(AppRoute.Login)
                    },
                    onShowSnackBar = { /* Handle globally or locally */ }
                )
            }
            is AppRoute.Login -> NavEntry(AppRoute.Login) {
                LoginScreen(
                    onNavigateToHome = { isPreferencesSet ->
                        backStack.clear()
                        if (isPreferencesSet) {
                            backStack.add(AppRoute.Dashboard())
                        } else {
                            backStack.add(AppRoute.Preferences)
                        }
                    },
                    onNavigateToSignUp = {
                        backStack.add(AppRoute.Register)
                    },
                    onShowSnackbar = { /* Handle */ }
                )
            }
            is AppRoute.Register -> NavEntry(AppRoute.Register) {
                RegisterScreen(
                    onNavigateToHome = { isPreferencesSet ->
                        backStack.clear()
                        if (isPreferencesSet) {
                            backStack.add(AppRoute.Dashboard())
                        } else {
                            backStack.add(AppRoute.Preferences)
                        }
                    },
                    onNavigateToLogin = {
                        backStack.remove(AppRoute.Register)
                    },
                    onShowSnackbar = { /* Handle */ }
                )
            }
            is AppRoute.Preferences -> NavEntry(AppRoute.Preferences) {
                PreferencesScreen(
                    viewModel = hiltViewModel(),
                    onNavigateToDashboard = { showDefaultAlert ->
                        backStack.clear()
                        backStack.add(AppRoute.Dashboard(showDefaultAlert = showDefaultAlert))
                    },
                    onShowSnackBar = { /* Handle */ }
                )
            }
            is AppRoute.Dashboard -> NavEntry(key) {
                MainScreen(
                    showDefaultAlert = key.showDefaultAlert,
                    onNavigateToPreferences = {
                        backStack.add(AppRoute.Preferences)
                    },
                    onNavigateToCourseDetails = { courseId ->
                        backStack.add(AppRoute.CourseDetails(courseId))
                    },
                    onNavigateToStudyRoom = { taskId, title, courseId, durationMinutes ->
                        backStack.add(AppRoute.StudyRoom(taskId, title, courseId, durationMinutes))
                    },
                    onNavigateToReadOnlyCourseDetails = { courseId ->
                        backStack.add(AppRoute.CourseDetails(courseId, allowEditing = false, showUploadMaterial = false))
                    },
                    onNavigateToLogin = {
                        backStack.clear()
                        backStack.add(AppRoute.Login)
                    },
                    onNavigateToAllTasks = { tasks ->
                        backStack.add(AppRoute.AllTasks(tasks))
                    },
                    onNavigateToTeamCourses = { teamId ->
                        backStack.add(AppRoute.TrackDetails(teamId))
                    }
                )
            }
            is AppRoute.Roadmap -> NavEntry(AppRoute.Roadmap) {
                RoadmapScreen(
                    innerPadding = PaddingValues(),
                    onNavigateToCourses = {
                        backStack.add(AppRoute.Dashboard())
                    }
                )
            }
            is AppRoute.Profile -> NavEntry(AppRoute.Profile) {
                ProfileScreen(
                    innerPadding = PaddingValues(),
                    viewModel = hiltViewModel(),
                    onNavigateToLogin = {
                        backStack.clear()
                        backStack.add(AppRoute.Login)
                    },
                    onShowSnackBar = { /* Handle */ }
                )
            }
            is AppRoute.CourseDetails -> NavEntry(key) {
                CourseDetailsScreen(
                    courseId = key.courseId, // ADD THIS LINE: Pass the ID from the sealed class[cite: 38, 39]
                    onNavigateBack = {
                        backStack.remove(key)
                    },
                    onNavigateToStudyRoom = { taskId, title, courseId, durationMinutes ->
                        backStack.add(AppRoute.StudyRoom(taskId, title, courseId, durationMinutes))
                    }
                )
            }
            is AppRoute.StudyRoom -> NavEntry(key) {
                StudyRoomScreen(
                    taskId = key.taskId,
                    title = key.title,
                    courseId = key.courseId,
                    durationMinutes = key.durationMinutes,
                    onNavigateBack = {
                        backStack.remove(key)
                    }
                )
            }
            is AppRoute.AllTasks -> NavEntry(key) {
                AllTasksScreen(
                    tasks = key.tasks,
                    onNavigateBack = {
                        backStack.remove(key)
                    },
                    onNavigateToStudyRoom = { taskId, title, courseId, durationMinutes ->
                        // Pass navigation to the Study Room directly from the All Tasks screen
                        backStack.add(AppRoute.StudyRoom(taskId, title, courseId, durationMinutes))
                    }
                )
            }
            is AppRoute.TrackDetails -> NavEntry(key) {
                TrackDetailsScreen(
                    teamId = key.teamId,
                    onNavigateBack = {
                        backStack.remove(key)
                    },
                    onNavigateToCourse = { courseId ->
                        backStack.add(AppRoute.CourseDetails(courseId, allowEditing = false, showUploadMaterial = false))
                    }
                )
            }
        }
    }
}
