package com.iti.mongez.data.di


import com.iti.mongez.data.repositories.auth.repository.MockAuthRepository
import com.iti.mongez.domain.auth.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AuthModule {

    @Binds
    abstract fun bindAuthRepository(
        mockAuthRepository: MockAuthRepository
    ): AuthRepository
}
