package com.example.lolaecu.data.model


import com.example.lolaecu.data.repository.PaymentRepository
import com.example.lolaecu.domain.model.MakeSaleRequest
import javax.inject.Inject

class OnlineCardPaymentUseCase @Inject constructor(
    private val paymentRepository: PaymentRepository,
) {
    suspend operator fun invoke(
        cardPaymentRequestBody: MakeSaleRequest
    ): NetworkResult<MakeSaleResponseModel> {
        return paymentRepository.makeOnlinePayment(cardPaymentRequestBody.toModel())
    }
}