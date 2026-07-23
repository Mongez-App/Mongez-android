package com.iti.mongez.data.di

import com.iti.mongez.data.repositories.courses.CoursesRepositoryImpl
import com.iti.mongez.data.sources.remote.services.CoursesApiService
import com.iti.mongez.domain.courses.repository.CoursesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoursesNetworkModule {

    @Provides
    @Singleton
    fun provideCoursesApiService(retrofit: Retrofit): CoursesApiService {
        return retrofit.create(CoursesApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class CoursesRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCoursesRepository(
        coursesRepositoryImpl: CoursesRepositoryImpl
    ): CoursesRepository
}