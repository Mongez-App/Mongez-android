package com.iti.mongez.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavEntry
import com.iti.mongez.domain.core.usecase.GetInitialRouteUseCase
import com.iti.mongez.domain.utils.StartDestination
import com.iti.mongez.presentation.auth.login.LoginScreen
import com.iti.mongez.presentation.auth.register.RegisterScreen
import com.iti.mongez.presentation.main.MainScreen
import com.iti.mongez.presentation.onboarding.view.OnboardingScreen
import com.iti.mongez.presentation.preferences.view.PreferencesScreen
import com.iti.mongez.presentation.preferences.viewmodel.PreferencesViewModel
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
                StartDestination.DASHBOARD -> AppRoute.Dashboard
            }
        }
    }
}

@Composable
fun AppNavHost(viewModel: NavViewModel = hiltViewModel()) {
    val startDestination by viewModel.startDestination.collectAsState()

    if (startDestination == null) {
        // Show loading while determining start destination
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val backStack = remember { mutableStateListOf(startDestination!!) }

    NavDisplay(
        backStack = backStack,
    ) { key ->
        when (key) {
            is AppRoute.Onboarding -> NavEntry(AppRoute.Onboarding) {
                OnboardingScreen(
                    viewModel = hiltViewModel(),
                    onNavigateToHome = {
                        backStack.clear()
                        backStack.add(AppRoute.Login)
                    },
                    onShowSnackBar = { /* Handle globally or locally */ }
                )
            }
            is AppRoute.Login -> NavEntry(AppRoute.Login) {
                LoginScreen(
                    onNavigateToHome = {
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
                    onNavigateToDashboard = {
                        backStack.clear()
                        backStack.add(AppRoute.Dashboard)
                    },
                    onShowSnackBar = { /* Handle */ }
                )
            }
            is AppRoute.Dashboard -> NavEntry(AppRoute.Dashboard) {
                MainScreen()
            }
            else -> NavEntry(key) {
                Text("Unknown Route")
            }
        }
    }
}
