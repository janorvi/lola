package com.example.lolaecu.core.utils

import com.example.lolaecu.domain.model.MakeSaleResponse
import com.example.lolaecu.domain.model.SaleDataDomain

object PaymentResponseBuilder {

    /** Build a successful payment response **/
    fun buildSuccessfulPaymentResponse(
        balance: Int,
        paymentRate: Int
    ): MakeSaleResponse = MakeSaleResponse(
        code = Constants.CARD_PAYMENT_SUCCESS_TRANSACTION_CODE,
        message = Constants.CARD_PAYMENT_SUCCESS_TRANSACTION_MESSAGE,
        data = SaleDataDomain(
            balance = balance,
            amount = paymentRate
        ),
        status = Constants.TRANSACTION_SUCCESS
    )

    /** No balance Payment response **/
    fun buildNoBalanceResponse(
        balance: Int,
        paymentRate: Int
    ): MakeSaleResponse = MakeSaleResponse(
        code = Constants.CARD_PAYMENT_NO_BALANCE_CODE,
        message = Constants.CARD_PAYMENT_NO_BALANCE_MESSAGE,
        status = Constants.TRANSACTION_INFO,
        data = SaleDataDomain(
            balance = balance,
            amount = paymentRate
        )
    )

    /** Unknown card payment response **/
    val unknownCardResponse: MakeSaleResponse = MakeSaleResponse(
        code = Constants.CARD_PAYMENT_UNKNOWN_CARD_CODE,
        message = Constants.CARD_PAYMENT_UNKNOWN_CARD_MESSAGE,
        status = Constants.TRANSACTION_WARNING
    )

    fun buildWarningPaymentResponse(
        code: String,
        message: String,
        status: String
    ): MakeSaleResponse = MakeSaleResponse(
        code = code,
        message = message,
        status = status
    )
}