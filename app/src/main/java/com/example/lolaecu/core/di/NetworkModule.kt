package com.example.lolaecu.core.di

import com.example.lolaecu.core.utils.Constants
import com.example.lolaecu.data.network.CashSaleApiClient
import com.example.lolaecu.data.network.ConfigApiClient
import com.example.lolaecu.data.network.ContentTypeInterceptor
import com.example.lolaecu.data.network.ResendTransactionApiClient
import com.example.lolaecu.data.network.SaleApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.LOLA_API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ContentTypeInterceptor())
            .build()
    }

    @Singleton
    @Provides
    fun provideSaleApiClient(retrofit: Retrofit): SaleApiClient {
        return retrofit.create(SaleApiClient::class.java)
    }

    @Singleton
    @Provides
    fun provideCashSaleApiClient(retrofit: Retrofit): CashSaleApiClient {
        return retrofit.create(CashSaleApiClient::class.java)
    }

    @Singleton
    @Provides
    fun provideConfigApiClient(retrofit: Retrofit): ConfigApiClient {
        return retrofit.create(ConfigApiClient::class.java)
    }

    @Singleton
    @Provides
    fun provideResendTransactionApiClient(retrofit: Retrofit): ResendTransactionApiClient {
        return retrofit.create(ResendTransactionApiClient::class.java)
    }
}