package com.iti.mongez.data.di

import com.iti.mongez.data.BuildConfig
import com.iti.mongez.data.core.network.AuthInterceptor
import com.iti.mongez.data.sources.remote.services.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit) : com.iti.mongez.data.sources.remote.services.AuthApiService {
        return retrofit.create(com.iti.mongez.data.sources.remote.services.AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRoadmapApiService(retrofit: Retrofit) : com.iti.mongez.data.sources.remote.services.RoadmapApiService {
        return retrofit.create(com.iti.mongez.data.sources.remote.services.RoadmapApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit) : com.iti.mongez.data.sources.remote.services.UserApiService {
        return retrofit.create(com.iti.mongez.data.sources.remote.services.UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideTasksApiService(retrofit: Retrofit) : com.iti.mongez.data.sources.remote.services.TasksApiService {
        return retrofit.create(com.iti.mongez.data.sources.remote.services.TasksApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideStudySessionApiService(retrofit: Retrofit) : com.iti.mongez.data.sources.remote.services.StudySessionApiService {
        return retrofit.create(com.iti.mongez.data.sources.remote.services.StudySessionApiService::class.java)
    }
}