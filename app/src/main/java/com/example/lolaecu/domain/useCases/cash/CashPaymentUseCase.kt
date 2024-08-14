package com.example.lolaecu.domain.useCases.cash

import com.example.lolaecu.data.model.CashSaleRequestModel
import com.example.lolaecu.data.model.CashSaleResponseModel
import com.example.lolaecu.data.model.NetworkResult
import com.example.lolaecu.data.network.CashSaleApiService
import com.example.lolaecu.domain.utils.HandleApi
import javax.inject.Inject

class CashPaymentUseCase @Inject constructor(
    private val cashSaleApiService: CashSaleApiService
) {
    suspend operator fun invoke(
        cashPaymentRequestBody: CashSaleRequestModel
    ): NetworkResult<CashSaleResponseModel> {
        return HandleApi.handlePostApi(cashPaymentRequestBody) {
            cashSaleApiService.makeCashSale(it)
        }
    }
}