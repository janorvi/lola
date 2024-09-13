package com.example.lolaecu.data.model

import com.google.gson.annotations.SerializedName


data class F21FrameResponseModel(
    @SerializedName("data") var data: DataModel = DataModel()
)

data class DataModel(
    @SerializedName("received_id") var receivedId: String = "",
    @SerializedName("code") var code: Int = 0,
    @SerializedName("green_list")
    var greenList: List<GreenListModel> = emptyList()
)

data class GreenListModel(
    @SerializedName("sn") var serialNumber: String = "",
    @SerializedName("b") var balance: Int = 0,
    @SerializedName("rtp") var rtp: String = "",
    @SerializedName("c") var credit: Int = 0,
    @SerializedName("s") var status: Int = 0,
    @SerializedName("pc") var pc: Int = 0
)