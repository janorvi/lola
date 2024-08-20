package com.example.lolaecu.domain.model

import com.example.lolaecu.data.model.MakeSaleResponseModel
import com.example.lolaecu.data.model.SaleData

data class MakeSaleResponse(
    var code: String = "",
    var message: String = "",
    var status: String = "",
    var data: SaleDataDomain = SaleDataDomain(),
    var creditEnable: String = "",
    var creditQuantity: String = "",
//    var transfer: Boolean = false,
    var transferQty: Int = 0,
    var transferTime: Int = 0
)

data class SaleDataDomain(
    var balance: Int = 0,
    var amount: Int = 0
)


fun MakeSaleResponseModel.toDomain() = MakeSaleResponse(
    code = code,
    message = message,
    status = status,
    data = data.toDomain(),
    creditEnable = creditEnable,
    creditQuantity = creditQuantity,
//    transfer = transfer,
    transferQty = transferQty,
    transferTime = transferTime
)

fun SaleData.toDomain() = SaleDataDomain(
    balance = balance,
    amount = amount
)