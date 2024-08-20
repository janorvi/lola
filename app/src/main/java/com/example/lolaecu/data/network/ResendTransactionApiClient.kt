package com.example.lolaecu.data.network

import com.example.lolaecu.data.model.MakeSaleRequestModel
import com.example.lolaecu.data.model.MakeSaleResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ResendTransactionApiClient {
    @POST("resend-transactions")
    suspend fun resendTransaction(
        @Body resendTransactionRequestBody: MakeSaleRequestModel
    ): Response<MakeSaleResponseModel>
}