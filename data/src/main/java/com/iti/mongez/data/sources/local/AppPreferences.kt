package com.iti.mongez.data.sources.local


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class AppPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object{
        private val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
    }


    suspend fun setOnboardingCompleted() {
        dataStore.edit { preferences ->
            preferences[IS_ONBOARDING_COMPLETED] = true
        }
    }

    suspend fun isOnboardingCompleted(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[IS_ONBOARDING_COMPLETED] ?: false // Default is false
        }.first()
    }
}