package com.example.lolaecu.data.network

import com.example.lolaecu.data.model.MakeSaleRequestModel
import com.example.lolaecu.data.model.ReversePaymentResponseModel
import retrofit2.Response
import javax.inject.Inject

class ReversePaymentApiService @Inject constructor(
    private val reversePaymentApiClient: ReversePaymentApiClient
) {
    suspend fun reversePayment(
        reversePaymentBody: MakeSaleRequestModel
    ): Response<ReversePaymentResponseModel> {
        return reversePaymentApiClient.reversePayment(reversePaymentBody)
    }
}