package com.example.lolaecu.data.network

import com.example.lolaecu.data.model.ConfigRequestModel
import com.example.lolaecu.data.model.ConfigResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ConfigApiClient {

    @POST("v2/config-telpo")
    suspend fun getConfig(
        @Body configRequestBody: ConfigRequestModel
    ): Response<ConfigResponseModel>
}