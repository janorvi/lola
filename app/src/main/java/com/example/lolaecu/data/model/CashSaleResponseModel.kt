package com.example.lolaecu.data.model

import com.google.gson.annotations.SerializedName

data class CashSaleResponseModel (

    @SerializedName("data" ) var data : SaleResponseData? = SaleResponseData()

)

data class SaleResponseData (

    @SerializedName("message"       ) var message       : String?           = "Success",
    @SerializedName("key"           ) var key           : String?           = "hPYMFREr49qtGOmMLNjwbfcj",
    @SerializedName("notifications" ) var notifications : ArrayList<String> = arrayListOf(),
    @SerializedName("message_tray"  ) var messageTray   : ArrayList<String> = arrayListOf()

)