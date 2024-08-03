package com.example.lolaecu.data.model

import com.google.gson.annotations.SerializedName
data class MakeSaleResponseModel(
    @SerializedName("code") var code: String = "",
    @SerializedName("message") var message: String = "",
    @SerializedName("status") var status: String = "",
    @SerializedName("data") var data: SaleData = SaleData(),
    @SerializedName("credit_enable") var creditEnable: String = "",
    @SerializedName("credit_quantity") var creditQuantity: String = "",
//    @SerializedName("transfer") var transfer: Boolean = false,
    @SerializedName("transfer_qty") var transferQty: Int = 0,
    @SerializedName("transfer_time") var transferTime: Int = 0
)
data class SaleData(
    @SerializedName("balance") var balance: Int = 0,
    @SerializedName("amount") var amount: Int = 0
)

