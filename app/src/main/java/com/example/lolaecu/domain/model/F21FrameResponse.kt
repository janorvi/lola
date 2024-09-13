package com.example.lolaecu.domain.model

import com.example.lolaecu.data.database.entities.GreenListEntity
import com.example.lolaecu.data.model.DataModel
import com.example.lolaecu.data.model.F21FrameResponseModel
import com.example.lolaecu.data.model.GreenListModel
import com.google.gson.annotations.SerializedName

data class F21FrameResponse(
    var data: DataDomain = DataDomain()
)

data class DataDomain(
    val receivedId: String? = null,
    val code: Int? = null,
    val greenList: List<GreenList> = listOf<GreenList>()
)

data class GreenList(
    @SerializedName("sn") var serialNumber: String = "",
    @SerializedName("b") var balance: Int = 0,
    @SerializedName("rtp") var rtp: String = "",
    @SerializedName("c") var credit: Int = 0,
    @SerializedName("s") var status: Int = 0,
    @SerializedName("pc") var pc: Int = 0
)

fun F21FrameResponseModel.toDomain() = F21FrameResponse(data.toDomain())

fun DataModel.toDomain() = DataDomain(
    receivedId = receivedId,
    code = code,
    greenList = greenList.map { it.toDomain() }
)

fun GreenListModel.toDomain() = GreenList(
    serialNumber = serialNumber,
    balance = balance,
    rtp = rtp,
    credit = credit,
    status = status,
    pc = pc
)

fun GreenListEntity.toDomain() = GreenList(
    serialNumber = serialNumber,
    balance = balance,
    rtp = rtp,
    credit = credit,
    status = status,
    pc = pc
)

fun GreenList.isNull(): Boolean = serialNumber == "" || rtp == ""

