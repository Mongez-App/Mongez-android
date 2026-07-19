package com.iti.mongez.data.preferences.sources

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.iti.mongez.domain.preferences.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private object PreferencesKeys {
        val DAILY_STUDY_HOURS = intPreferencesKey("daily_study_hours")
        val AVAILABLE_DAYS = stringSetPreferencesKey("available_days")
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

    suspend fun savePreferences(preferences: UserPreferences) {
        dataStore.edit { prefs ->
            prefs[PreferencesKeys.DAILY_STUDY_HOURS] = preferences.dailyStudyHours
            prefs[PreferencesKeys.AVAILABLE_DAYS] = preferences.availableDays.toSet()
        }
    }
}
