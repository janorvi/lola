package com.example.lolaecu.data.network

import com.example.lolaecu.data.model.CashSaleRequestModel
import com.example.lolaecu.data.model.CashSaleResponseModel
import retrofit2.Response
import javax.inject.Inject

class CashSaleApiService @Inject constructor(
    private val cashSaleApiClient: CashSaleApiClient
) {
    suspend fun makeCashSale(
        makeCashSaleBody: CashSaleRequestModel
    ): Response<CashSaleResponseModel> {
        return cashSaleApiClient.makeCashSale(makeCashSaleBody)
    }
}