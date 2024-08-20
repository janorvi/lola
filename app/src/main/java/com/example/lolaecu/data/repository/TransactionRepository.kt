package com.example.lolaecu.data.repository

import com.example.lolaecu.data.model.MakeSaleRequestModel
import com.example.lolaecu.data.model.MakeSaleResponseModel
import com.example.lolaecu.data.model.NetworkResult
import com.example.lolaecu.data.network.ResendTransactionApiService
import com.example.lolaecu.domain.utils.HandleApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TransactionRepository @Inject constructor(
    private val resendTransactionApiService: ResendTransactionApiService
) {

    val resendTransactionFlow: Flow<Boolean> = flow {
        while (true) {
            delay(60000)
            emit(true)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun resendTransaction(
        resendTransactionRequestBody: MakeSaleRequestModel
    ): NetworkResult<MakeSaleResponseModel> {
        return HandleApi.handlePostApi(resendTransactionRequestBody) {
            resendTransactionApiService.resendTransaction(it)
        }
    }
}