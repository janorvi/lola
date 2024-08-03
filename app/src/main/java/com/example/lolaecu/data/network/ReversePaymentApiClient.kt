package com.example.lolaecu.data.network

import com.example.lolaecu.data.model.MakeSaleRequestModel
import com.example.lolaecu.data.model.ReversePaymentResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ReversePaymentApiClient {
    @POST("reverse-transactions")
    suspend fun reversePayment(
        @Body reversePayment: MakeSaleRequestModel
    ): Response<ReversePaymentResponseModel>
}