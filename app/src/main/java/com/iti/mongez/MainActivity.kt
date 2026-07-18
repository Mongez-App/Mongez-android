package com.iti.mongez

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.automirrored.outlined.MenuBook
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.iti.mongez.designsystem.components.navigation.AppNavigationBar
import com.iti.mongez.designsystem.components.navigation.AppNavigationBarItem
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import dagger.hilt.android.AndroidEntryPoint
import com.iti.mongez.domain.core.Result
import com.iti.mongez.domain.onboarding.usecase.CheckOnboardingStatusUseCase
import com.iti.mongez.domain.auth.usecase.CheckAuthStatusUseCase
import com.iti.mongez.presentation.onboarding.view.OnboardingScreen
import com.iti.mongez.presentation.onboarding.viewmodel.OnboardingViewModel
import com.iti.mongez.presentation.auth.login.LoginScreen
import com.iti.mongez.presentation.auth.register.RegisterScreen
import com.iti.mongez.presentation.preferences.view.PreferencesScreen
import com.iti.mongez.presentation.preferences.viewmodel.PreferencesViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Root Activity — applies [MongezTheme] and sets up the main scaffold
 * with navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var checkOnboardingStatusUseCase: CheckOnboardingStatusUseCase

    @Inject
    lateinit var checkAuthStatusUseCase: CheckAuthStatusUseCase

    private var isInitialStateLoading by mutableStateOf(true)
    private var startDestination by mutableStateOf("onboarding")

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.setKeepOnScreenCondition {
            isInitialStateLoading
        }

        lifecycleScope.launch {
            val onboardingResult = checkOnboardingStatusUseCase()
            val isOnboardingCompleted = onboardingResult is Result.Success && onboardingResult.data
            
            if (isOnboardingCompleted) {
                val authResult = checkAuthStatusUseCase()
                val isLoggedIn = authResult is Result.Success && authResult.data
                startDestination = if (isLoggedIn) "main" else "login"
            } else {
                startDestination = "onboarding"
            }
            
            isInitialStateLoading = false
        }

        enableEdgeToEdge()
        setContent {
            MongezTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()
                if (!isInitialStateLoading) {
                    NavHost(
                        navController = navController,
                        startDestination = startDestination
                    ) {
                        composable("onboarding") {
                            val onboardingViewModel: OnboardingViewModel = hiltViewModel()
                            OnboardingScreen(
                                viewModel = onboardingViewModel,
                                onNavigateToHome = {
                                    navController.navigate("login") {
                                        popUpTo("onboarding") { inclusive = true }
                                    }
                                },
                                onShowSnackBar = { message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message)
                                    }
                                }
                            )
                        }
                        composable("login") {
                            LoginScreen(
                                onNavigateToHome = {
                                    navController.navigate("preferences") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                },
                                onNavigateToSignUp = {
                                    navController.navigate("register")
                                },
                                onShowSnackbar = { message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message)
                                    }
                                }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onNavigateToHome = {
                                    navController.navigate("preferences") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                },
                                onNavigateToLogin = {
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                },
                                onShowSnackbar = { message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message)
                                    }
                                }
                            )
                        }
                        composable("preferences") {
                            val preferencesViewModel: PreferencesViewModel = hiltViewModel()
                            PreferencesScreen(
                                viewModel = preferencesViewModel,
                                onNavigateToDashboard = {
                                    navController.navigate("main") {
                                        popUpTo("preferences") { inclusive = true }
                                    }
                                },
                                onShowSnackBar = { message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message)
                                    }
                                }
                            )
                        }
                        composable("main") {
                            MainScreen(snackbarHostState)
                        }
                    }
                }
            }
        }
    }
}

// ────────────────────────────────────────────────────────────────
// Navigation Items
// ────────────────────────────────────────────────────────────────

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

// ────────────────────────────────────────────────────────────────
// Main Screen
// ────────────────────────────────────────────────────────────────

@Composable
private fun MainScreen(snackbarHostState: SnackbarHostState = remember { SnackbarHostState() }) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Theme.colorScheme.surface.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AppNavigationBar(
                items = navigationItems,
                selectedIndex = selectedTab,
                onItemSelected = { selectedTab = it },
            )
        },
    ) { innerPadding ->
        // Placeholder content — feature screens will be wired here via Navigation
        Text(
            text = "Welcome to Mongez! — ${navigationItems[selectedTab].label}",
            style = Theme.typography.title.large,
            color = Theme.colorScheme.text.primary,
            modifier = Modifier.padding(innerPadding).padding(Theme.spacing.xl),
        )
    }
}

// ────────────────────────────────────────────────────────────────
// Preview
// ────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Main Screen — Light")
@Composable
private fun MainScreenPreview() {
    MongezTheme {
        MainScreen()
    }
}

@Preview(showBackground = true, name = "Main Screen — Dark")
@Composable
private fun MainScreenDarkPreview() {
    MongezTheme(darkTheme = true) {
        MainScreen()
    }
}
