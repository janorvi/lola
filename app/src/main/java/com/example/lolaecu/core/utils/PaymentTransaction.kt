package com.example.lolaecu.core.utils

import com.example.lolaecu.domain.model.MakeSaleRequest
object PaymentTransaction {

    private var paymentTransaction: MakeSaleRequest = MakeSaleRequest()

    /** reset the MakeSaleRequest object to avoid using an old object **/
    fun resetPaymentTransaction() {
        this.paymentTransaction = MakeSaleRequest()
    }

    /** Save the current MakeSaleRequest object  **/
    fun setPaymentTransaction(paymentRequest: MakeSaleRequest) {
        this.paymentTransaction = paymentRequest
    }

    /** get the current MakeSaleRequest object **/
    fun getPaymentTransaction(): MakeSaleRequest {
        return this.paymentTransaction
    }
}