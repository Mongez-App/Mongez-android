package com.iti.mongez.data.di

import com.iti.mongez.data.repositories.studyroom.StudyRoomRepositoryImpl
import com.iti.mongez.domain.studyroom.repository.StudyRoomRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class StudyRoomRepositoryModule {
    @Binds
    @Singleton
    abstract fun bindStudyRoomRepository(
        studyRoomRepositoryImpl: StudyRoomRepositoryImpl
    ): StudyRoomRepository
}
