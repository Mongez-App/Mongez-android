package com.iti.mongez.data.di

import com.iti.mongez.data.repositories.roadmap.RoadmapRepositoryImpl
import com.iti.mongez.domain.roadmap.repository.RoadmapRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RoadmapRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindRoadmapRepository(
        roadmapRepositoryImpl: RoadmapRepositoryImpl
    ): RoadmapRepository
}
