package com.iti.mongez.data.di

import com.iti.mongez.data.repositories.settings.AppSettingsRepositoryImpl
import com.iti.mongez.domain.settings.repository.AppSettingsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    @Singleton
    abstract fun bindAppSettingsRepository(
        impl: AppSettingsRepositoryImpl
    ): AppSettingsRepository
}
