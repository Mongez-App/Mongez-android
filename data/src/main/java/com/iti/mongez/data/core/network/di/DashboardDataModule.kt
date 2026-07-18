package com.iti.mongez.data.core.network.di

import com.iti.mongez.data.dashboard.remote.DashboardApiService
import com.iti.mongez.data.dashboard.repository.DashboardRepositoryImpl
import com.iti.mongez.domain.dashboard.repository.DashboardRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DashboardNetworkModule {

    @Provides
    @Singleton
    fun provideDashboardApiService(retrofit: Retrofit): DashboardApiService {
        return retrofit.create(DashboardApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DashboardRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDashboardRepository(
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ): DashboardRepository
}