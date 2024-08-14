package com.example.lolaecu.data.network

import com.example.lolaecu.data.model.CashSaleRequestModel
import com.example.lolaecu.data.model.CashSaleResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CashSaleApiClient {

    @POST("v1/sale-mke")
    suspend fun makeCashSale(
        @Body makeCashSaleRequestBody: CashSaleRequestModel
    ): Response<CashSaleResponseModel>

}