package com.iti.mongez.data.di

import com.iti.mongez.data.repositories.organization.OrganizationRepositoryImpl
import com.iti.mongez.data.sources.remote.impl.OrganizationRemoteDataSourceImpl
import com.iti.mongez.data.sources.remote.interfaces.OrganizationRemoteDataSource
import com.iti.mongez.data.sources.remote.services.OrganizationApiService
import com.iti.mongez.domain.organization.repository.OrganizationRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OrganizationNetworkModule {
    @Provides
    @Singleton
    fun provideOrganizationApiService(retrofit: Retrofit): OrganizationApiService {
        return retrofit.create(OrganizationApiService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
abstract class OrganizationRepositoryModule {
    @Binds
    abstract fun bindOrganizationRemoteDataSource(
        impl: OrganizationRemoteDataSourceImpl
    ): OrganizationRemoteDataSource

    @Binds
    abstract fun bindOrganizationRepository(
        impl: OrganizationRepositoryImpl
    ): OrganizationRepository
}
