package com.example.lolaecu.data.network

import com.example.lolaecu.data.model.ConfigRequestModel
import com.example.lolaecu.data.model.ConfigResponseModel
import retrofit2.Response
import javax.inject.Inject

class ConfigApiService @Inject constructor(
    private val apiClient: ConfigApiClient
) {

    suspend fun getConfig(configRequestBody: ConfigRequestModel): Response<ConfigResponseModel> {
            return apiClient.getConfig(configRequestBody)
    }
}