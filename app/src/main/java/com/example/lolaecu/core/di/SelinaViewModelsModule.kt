package com.example.lolaecu.core.di

import android.app.Application
import com.example.mdt.repository.APICallsCycleRepository
import com.example.mdt.repository.DatabaseRepository
import com.example.mdt.repository.HardwareRepository
import com.example.mdt.repository.ServiceRepository
import com.example.mdt.viewmodel.ApplicationViewModel
import com.example.mdt.viewmodel.ComunicationsViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SelinaViewModelsModule {

    @Provides
    @Singleton
    fun provideSelinaApplicationViewModel(
        databaseRepository: DatabaseRepository,
        hardwareRepository: HardwareRepository,
        serviceRepository: ServiceRepository,
        apiCallsCycleRepository: APICallsCycleRepository
    ): ApplicationViewModel {
        return ApplicationViewModel(
            databaseRepository = databaseRepository,
            hardwareRepository = hardwareRepository,
            serviceRepository = serviceRepository,
            apiCallsCycleRepository = apiCallsCycleRepository
        )
    }

    @Provides
    @Singleton
    fun provideCommunicationsViewModel(application: Application): ComunicationsViewModel {
        return ComunicationsViewModel(application)
    }

//    @Provides
//    @Singleton
//    fun provideApplication(): Application {
//        return Application()
//    }

}