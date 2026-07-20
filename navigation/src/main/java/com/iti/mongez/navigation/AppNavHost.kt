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
import com.iti.mongez.feature.coursedetails.view.CourseDetailsScreen
import com.iti.mongez.presentation.auth.login.view.LoginScreen
import com.iti.mongez.presentation.auth.register.view.RegisterScreen
import com.iti.mongez.presentation.main.MainScreen
import com.iti.mongez.presentation.onboarding.view.OnboardingScreen
import androidx.compose.foundation.layout.PaddingValues
import com.iti.mongez.presentation.preferences.view.PreferencesScreen
import com.iti.mongez.presentation.roadmap.view.RoadmapScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavViewModel @Inject constructor(
    private val getInitialRouteUseCase: GetInitialRouteUseCase
) : ViewModel() {

    private val _startDestination = MutableStateFlow<AppRoute?>(null)
    val startDestination: StateFlow<AppRoute?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val destination = getInitialRouteUseCase()
            _startDestination.value = when (destination) {
                StartDestination.ONBOARDING -> AppRoute.Onboarding
                StartDestination.LOGIN -> AppRoute.Login
                StartDestination.DASHBOARD -> AppRoute.Dashboard()
            }
        }
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
                    onNavigateToPreferences = {
                        backStack.clear()
                        backStack.add(AppRoute.Preferences)
                    },
                    onNavigateToSignUp = {
                        backStack.add(AppRoute.Register)
                    },
                    onShowSnackbar = { /* Handle */ }
                )
            }
            is AppRoute.Register -> NavEntry(AppRoute.Register) {
                RegisterScreen(
                    onNavigateToHome = {
                        backStack.clear()
                        backStack.add(AppRoute.Preferences)
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
                    showDefaultAlert = (key).showDefaultAlert,
                    onNavigateToPreferences = {
                        //To-Do go to the profile
                    },
                    onNavigateToCourseDetails = { courseId ->
                        backStack.add(AppRoute.CourseDetails(courseId))
                    }
                )
            }
            is AppRoute.Roadmap -> NavEntry(AppRoute.Roadmap) {
                RoadmapScreen(
                    innerPadding = PaddingValues(),
                    onNavigateToBlockDetails = {
                        //To-Do
                    },
                    onNavigateToAddEvent = {
                        //To-Do
                    }
                )
            }
            is AppRoute.CourseDetails -> NavEntry(key) {
                CourseDetailsScreen(
                    onNavigateBack = {
                        backStack.remove(key)
                    }
                )
            }
        }
    }
}
