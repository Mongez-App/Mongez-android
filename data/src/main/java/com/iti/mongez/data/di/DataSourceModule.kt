package com.iti.mongez.data.di

import com.iti.mongez.data.sources.remote.impl.*
import com.iti.mongez.data.sources.remote.interfaces.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindAuthRemoteDataSource(
        impl: AuthRemoteDataSourceImpl
    ): AuthRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCalendarRemoteDataSource(
        impl: CalendarRemoteDataSourceImpl
    ): CalendarRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindCoursesRemoteDataSource(
        impl: CoursesRemoteDataSourceImpl
    ): CoursesRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindDashboardRemoteDataSource(
        impl: DashboardRemoteDataSourceImpl
    ): DashboardRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindRoadmapRemoteDataSource(
        impl: RoadmapRemoteDataSourceImpl
    ): RoadmapRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindUserRemoteDataSource(
        impl: UserRemoteDataSourceImpl
    ): UserRemoteDataSource

}
