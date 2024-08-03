package com.example.lolaecu.data.repository

import com.example.lolaecu.data.model.MakeSaleRequestModel
import com.example.lolaecu.data.model.NetworkResult
import com.example.lolaecu.data.model.ReversePaymentResponseModel
import com.example.lolaecu.data.network.ReversePaymentApiService
import com.example.lolaecu.domain.utils.HandleApi
import javax.inject.Inject

class ReversePaymentRepository @Inject constructor(
    private val reversePayment: ReversePaymentApiService
) {
    suspend fun reversePayment(
        reversePaymentBody: MakeSaleRequestModel
    ): NetworkResult<ReversePaymentResponseModel> {
        return HandleApi.handlePostApi(reversePaymentBody) {
            reversePayment.reversePayment(it)
        }
    }
}