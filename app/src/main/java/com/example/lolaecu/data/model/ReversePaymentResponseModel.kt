package com.example.lolaecu.data.model

import com.google.gson.annotations.SerializedName

data class ReversePaymentResponseModel(
    @SerializedName("status") var status: Boolean = false
)
