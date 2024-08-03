package com.example.lolaecu.data.network

import com.example.lolaecu.data.model.MakeSaleRequestModel
import com.example.lolaecu.data.model.MakeSaleResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SaleApiClient {
    @POST("v1/sale-mke")
    suspend fun makeSale(
        @Body makeSaleRequestBody: MakeSaleRequestModel
    ): Response<MakeSaleResponseModel>
}