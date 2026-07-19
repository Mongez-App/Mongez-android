package com.iti.mongez.data.di


import com.iti.mongez.data.repositories.dashboard.DashboardRepositoryImpl
import com.iti.mongez.domain.dashboard.repository.DashboardRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ): DashboardRepository
}