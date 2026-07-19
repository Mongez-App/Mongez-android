package com.iti.mongez.data.di


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// 1. Define the DataStore delegate at the top level of the file
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "")

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    // 2. Provide the DataStore instance to Hilt
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}