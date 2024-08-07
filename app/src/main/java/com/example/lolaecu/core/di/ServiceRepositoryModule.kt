package com.example.lolaecu.core.di

import com.example.mdt.repository.ServiceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceRepositoryModule {
    @Provides
    @Singleton
    fun provideServiceRepository(): ServiceRepository {
        return ServiceRepository()
    }
}