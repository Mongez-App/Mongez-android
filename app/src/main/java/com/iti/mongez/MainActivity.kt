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
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.iti.mongez.domain.settings.model.Language
import com.iti.mongez.worker.CalendarSyncWorker
import java.util.Locale
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val navViewModel: NavViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        
        scheduleCalendarSync()

        splashScreen.setKeepOnScreenCondition {
            navViewModel.startDestination.value == null
        }

        enableEdgeToEdge()
        setContent {
            val settings by navViewModel.appSettings.collectAsState()
            
            val context = LocalContext.current
            val configuration = LocalConfiguration.current
            
            val locale = when (settings.language) {
                Language.SYSTEM -> Locale.getDefault()
                Language.EN -> Locale.forLanguageTag("en")
                Language.AR -> Locale.forLanguageTag("ar")
            }
            
            val updatedConfiguration = Configuration(configuration).apply {
                setLocale(locale)
                setLayoutDirection(locale)
            }
            
            val layoutDirection = if (updatedConfiguration.layoutDirection == android.view.View.LAYOUT_DIRECTION_RTL) {
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }

            val localeContext = remember(updatedConfiguration) {
                context.createConfigurationContext(updatedConfiguration)
            }

            val wrappedContext = remember(localeContext) {
                object : ContextWrapper(localeContext) {
                    override fun getBaseContext(): Context = context
                }
            }

            CompositionLocalProvider(
                LocalConfiguration provides updatedConfiguration,
                LocalContext provides wrappedContext,
                LocalLayoutDirection provides layoutDirection
            ) {
                MongezTheme(darkTheme = settings.isDarkModeEnabled) {
                    AppNavHost()
                }
            }
        }
    }

    private fun scheduleCalendarSync() {
        val syncRequest = PeriodicWorkRequestBuilder<CalendarSyncWorker>(30, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "CalendarSync",
            ExistingPeriodicWorkPolicy.KEEP,
            syncRequest
        )
    }
}

