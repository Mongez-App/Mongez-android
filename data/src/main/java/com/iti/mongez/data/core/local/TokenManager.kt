package com.iti.mongez.data.core.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    // Expose the token as a Flow for reactive architecture
    val tokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY]
    }

    // Call this inside your login/signup repository implementation when the backend returns a token
    suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // Call this during sign-out
    suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }
}