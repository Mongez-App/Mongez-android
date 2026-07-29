package com.iti.mongez.data.sources.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.iti.mongez.domain.preferences.model.UserPreferences
import com.iti.mongez.domain.settings.model.AppSettings
import com.iti.mongez.domain.settings.model.Language
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val DAILY_STUDY_HOURS = intPreferencesKey("daily_study_hours")
        val AVAILABLE_DAYS = stringSetPreferencesKey("available_days")
        
        val CALENDAR_SYNC = booleanPreferencesKey("calendar_sync")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val LANGUAGE = stringPreferencesKey("language")
        val PREFERENCES_ONBOARDING_COMPLETED = booleanPreferencesKey("preferences_onboarding_completed")
    }

    val userPreferencesFlow: Flow<UserPreferences?> = dataStore.data.map { preferences ->
        val hours = preferences[PreferencesKeys.DAILY_STUDY_HOURS]
        val days = preferences[PreferencesKeys.AVAILABLE_DAYS]

        if (hours != null && days != null) {
            UserPreferences(
                dailyStudyHours = hours,
                availableDays = days.toList()
            )
        } else {
            null
        }
    }

    val appSettingsFlow: Flow<AppSettings> = dataStore.data.map { preferences ->
        AppSettings(
            isCalendarSyncEnabled = preferences[PreferencesKeys.CALENDAR_SYNC] ?: false,
            isDarkModeEnabled = preferences[PreferencesKeys.DARK_MODE] ?: false,
            language = Language.entries.find { it.code == preferences[PreferencesKeys.LANGUAGE] } ?: Language.EN
        )
    }

    val isOnboardingCompletedFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.PREFERENCES_ONBOARDING_COMPLETED] ?: false
    }

    suspend fun savePreferences(preferences: UserPreferences) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.DAILY_STUDY_HOURS] = preferences.dailyStudyHours
            prefs[PreferencesKeys.AVAILABLE_DAYS] = preferences.availableDays.toSet()
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.PREFERENCES_ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun updateAppSettings(settings: AppSettings) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.CALENDAR_SYNC] = settings.isCalendarSyncEnabled
            prefs[PreferencesKeys.DARK_MODE] = settings.isDarkModeEnabled
            prefs[PreferencesKeys.LANGUAGE] = settings.language.code
        }
    }
}