package com.example.lolaecu.core.di

import android.content.Context
import com.example.mdt.database.ApplicationDatabase
import com.example.mdt.repository.DatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideApplicationDatabase(@ApplicationContext context: Context): ApplicationDatabase {
        return ApplicationDatabase.getInstance(context)!!
    }

    @Provides
    @Singleton
    fun provideDatabaseRepository(
        applicationDatabase: ApplicationDatabase
    ): DatabaseRepository {
        return DatabaseRepository(
            configurationDAO = applicationDatabase.configurationDao(),
            statesDAO = applicationDatabase.statesDao(),
            rateDAO = applicationDatabase.rateDAO(),
            transactionDAO = applicationDatabase.transactionDAO(),
            notificationDAO = applicationDatabase.notificationDAO(),
            messageDAO = applicationDatabase.messageDAO(),
            hubTransactionDAO = applicationDatabase.hubTransactionDAO(),
            modeAListsDao = applicationDatabase.ModeAListsDAO()
        )
    }
}