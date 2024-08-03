package com.example.lolaecu.domain.useCases.frames

import com.example.lolaecu.data.model.ConfigRequestModel
import com.example.lolaecu.data.model.ConfigResponseModel
import com.example.lolaecu.data.model.NetworkResult
import com.example.lolaecu.data.network.ConfigApiService
import com.example.lolaecu.domain.utils.HandleApi
import javax.inject.Inject

class GetConfigUseCase @Inject constructor(
    private val configApiService: ConfigApiService
) {
    suspend fun getConfig(
        configRequestBody: ConfigRequestModel
    ): NetworkResult<ConfigResponseModel> {

        return HandleApi.handlePostApi(configRequestBody) {
            configApiService.getConfig(it)
        }
    }
}