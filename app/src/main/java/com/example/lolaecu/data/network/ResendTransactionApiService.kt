package com.example.lolaecu.data.network

import com.example.lolaecu.data.model.MakeSaleRequestModel
import com.example.lolaecu.data.model.MakeSaleResponseModel
import retrofit2.Response
import javax.inject.Inject

class ResendTransactionApiService @Inject constructor(
    private val resendTransactionApiClient: ResendTransactionApiClient
) {
    suspend fun resendTransaction(
        resendTransactionBody: MakeSaleRequestModel
    ): Response<MakeSaleResponseModel> {
        return resendTransactionApiClient.resendTransaction(resendTransactionBody)
    }
}