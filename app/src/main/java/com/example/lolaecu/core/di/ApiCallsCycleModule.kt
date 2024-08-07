package com.example.lolaecu.core.di

import com.example.mdt.repository.APICallsCycleRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiCallsCycleModule {

    @Provides
    @Singleton
    fun provideAPICallsCycleRepository(): APICallsCycleRepository {
        return APICallsCycleRepository()
    }
}