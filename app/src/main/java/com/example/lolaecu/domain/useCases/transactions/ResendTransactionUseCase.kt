package com.example.lolaecu.domain.useCases.transactions

import com.example.lolaecu.data.model.MakeSaleRequestModel
import com.example.lolaecu.data.model.MakeSaleResponseModel
import com.example.lolaecu.data.model.NetworkResult
import com.example.lolaecu.data.repository.TransactionRepository
import javax.inject.Inject

class ResendTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {
    suspend operator fun invoke(transactionBody: MakeSaleRequestModel)
    : NetworkResult<MakeSaleResponseModel> {
        return transactionRepository.resendTransaction(transactionBody)
    }
}