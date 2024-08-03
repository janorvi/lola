package com.example.lolaecu.domain.useCases.qr

import com.example.lolaecu.data.model.MakeSaleRequestModel
import com.example.lolaecu.data.model.MakeSaleResponseModel
import com.example.lolaecu.data.model.NetworkResult
import com.example.lolaecu.data.network.SaleApiService
import com.example.lolaecu.domain.utils.HandleApi
import javax.inject.Inject

class QRPaymentUseCase @Inject constructor(
    private val saleApiService: SaleApiService
) {

    suspend fun qrPaymentUseCase(
        qrPaymentRequestBody: MakeSaleRequestModel
    ): NetworkResult<MakeSaleResponseModel> {
        return HandleApi.handlePostApi(qrPaymentRequestBody) {
            saleApiService.makeSale(it)
        }
    }
}