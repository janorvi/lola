package com.example.lolaecu.core.di

import android.content.Context
import com.telpo.tps550.api.nfc.Nfc
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TPS530Module {
    @Provides
    @Singleton
    fun provideNfc(@ApplicationContext context: Context): Nfc {
        return Nfc(context)
    }
}