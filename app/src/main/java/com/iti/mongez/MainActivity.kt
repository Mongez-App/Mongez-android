package com.iti.mongez

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.navigation.AppNavHost
import com.iti.mongez.navigation.NavViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Root Activity — applies [MongezTheme] and sets up the main scaffold
 * with navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val navViewModel: NavViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen visible until the initial route is resolved
        // from DataStore. The condition is checked on every frame draw.
        splashScreen.setKeepOnScreenCondition {
            navViewModel.startDestination.value == null
        }

        enableEdgeToEdge()
        setContent {
            MongezTheme {
                AppNavHost()
            }
        }
    }
}

// Cleaned up MainScreen and items

