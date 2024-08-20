package com.example.lolaecu.data.repository

import com.example.lolaecu.data.model.MakeSaleRequestModel
import com.example.lolaecu.data.model.MakeSaleResponseModel
import com.example.lolaecu.data.model.NetworkResult
import com.example.lolaecu.data.network.ResendTransactionApiService
import com.example.lolaecu.data.network.SaleApiService
import com.example.lolaecu.domain.utils.HandleApi
import javax.inject.Inject

class PaymentRepository @Inject constructor(
    private val saleApiService: SaleApiService,
    private val resendTransactionApiService: ResendTransactionApiService
) {

    suspend fun makeOnlinePayment(
        paymentRequestBody: MakeSaleRequestModel
    ): NetworkResult<MakeSaleResponseModel> {
        return HandleApi.handlePostApi(paymentRequestBody) {
            saleApiService.makeSale(it)
        }
    }

    suspend fun resendTransaction(
        transactionRequestBody: MakeSaleRequestModel
    ): NetworkResult<MakeSaleResponseModel> {
        return HandleApi.handlePostApi(transactionRequestBody) {
            resendTransactionApiService.resendTransaction(it)
        }
    }


}