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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.iti.mongez.designsystem.components.navigation.AppNavigationBar
import com.iti.mongez.designsystem.components.navigation.AppNavigationBarItem
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.designsystem.theme.Theme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Root Activity — applies [MongezTheme] and sets up the main scaffold
 * with bottom navigation.
 */
class MainActivity : ComponentActivity() {
    private var isInitialStateLoading = true
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition {
            isInitialStateLoading
        }
        lifecycleScope.launch {
            delay(1500) // Replace with actual loading logic
            isInitialStateLoading = false
        }
        enableEdgeToEdge()
        setContent {
            MongezTheme {
                MainScreen()
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
private fun MainScreen() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Theme.colorScheme.surface.background,
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