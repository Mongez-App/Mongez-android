package com.iti.mongez.data.sources.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val TOKEN_KEY = stringPreferencesKey("auth_token")
    }

    private var cachedToken: String? = null

    // Expose the token as a Flow for reactive architecture
    val tokenFlow: Flow<String?> = dataStore.data.map { preferences ->
        preferences[TOKEN_KEY].also { cachedToken = it }
    }

    // Synchronous access to the current token (avoids DataStore flow delays)
    fun getToken(): String? = cachedToken

    // Call this inside your login/signup repository implementation when the backend returns a token
    suspend fun saveToken(token: String) {
        cachedToken = token
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = token
        }
    }

    // Call this during sign-out
    suspend fun clearToken() {
        cachedToken = null
        dataStore.edit { preferences ->
            preferences.remove(TOKEN_KEY)
        }
    }

    // Check if a token is present
    suspend fun hasToken(): Boolean {
        return !dataStore.data.map { preferences ->
            preferences[TOKEN_KEY]
        }.first().isNullOrEmpty()
    }
}