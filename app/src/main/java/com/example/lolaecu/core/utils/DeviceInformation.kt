package com.example.lolaecu.core.utils

object DeviceInformation {
    var deviceModel = ""
    private var deviceID: String = ""
    var isTransactionsOffline: Boolean = false
    var transactionPlotId: String = ""
    var latestGreenListRtp: String = "0"
    var isCardBeingRead: Boolean = false


    fun setDeviceId(imei: String) {
        this.deviceID = imei
    }

    fun getDeviceId(): String {
        return this.deviceID
    }
}