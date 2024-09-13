package com.example.lolaecu.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lolaecu.data.model.GreenListModel
import com.example.lolaecu.domain.model.GreenList

@Entity(tableName = "green_list_table")
data class GreenListEntity(
    @PrimaryKey
    @ColumnInfo(name = "sn") var serialNumber: String = "",
    @ColumnInfo(name = "b") var balance: Int = 0,
    @ColumnInfo(name = "rtp") var rtp: String = "",
    @ColumnInfo(name = "c") var credit: Int = 0,
    @ColumnInfo(name = "s") var status: Int = 0,
    @ColumnInfo(name = "pc") var pc: Int = 0
)

fun GreenListModel.toEntity() = GreenListEntity(
    serialNumber = serialNumber,
    balance = balance,
    rtp = rtp,
    credit = credit,
    status = status,
    pc = pc
)

fun GreenList.toEntity() = GreenListEntity(
    serialNumber = serialNumber,
    balance = balance,
    rtp = rtp,
    credit = credit,
    status = status,
    pc = pc
)
