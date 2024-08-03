package com.example.lolaecu.data.network

import com.example.lolaecu.data.model.MakeSaleRequestModel
import com.example.lolaecu.data.model.MakeSaleResponseModel
import retrofit2.Response
import javax.inject.Inject

class SaleApiService @Inject constructor(
    private val saleApiClient: SaleApiClient
) {
    suspend fun makeSale(
        makeSaleBody: MakeSaleRequestModel
    ): Response<MakeSaleResponseModel> {
        return saleApiClient.makeSale(makeSaleBody)
    }
}