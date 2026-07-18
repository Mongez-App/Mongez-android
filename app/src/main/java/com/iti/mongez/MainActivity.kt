package com.iti.mongez

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.iti.mongez.designsystem.theme.MongezTheme
import com.iti.mongez.navigation.AppNavHost
import dagger.hilt.android.AndroidEntryPoint

/**
 * Root Activity — applies [MongezTheme] and sets up the main scaffold
 * with navigation.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            MongezTheme {
                AppNavHost()
            }
        }
    }
}

// Cleaned up MainScreen and items

