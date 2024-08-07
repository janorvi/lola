package com.example.lolaecu.core.di

import android.content.Context
import com.example.mdt.repository.HardwareRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HardwareRepositoryModule {
    @Provides
    @Singleton
    fun provideHardwareRepository(@ApplicationContext context: Context): HardwareRepository {
        return HardwareRepository(context)
    }
}