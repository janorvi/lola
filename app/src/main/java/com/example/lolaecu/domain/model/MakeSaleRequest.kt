package com.example.lolaecu.domain.model
data class MakeSaleRequest(
    var creditEnable: String = "0",
    var creditUse: String = "0",
    var cur: String = "0",
    var date: String = "1695180099",
    var driver: String = "265",
    var employeeFullName: String = "TCMR TECN2",
    var hw: String = "1111",
    var imei: String = "",
    var latitude: String = "4.69005",
    var longitude: String = "-74.0708",
    var offline: String = "0",
    var plotId: String = "b55d88d11eee9799995cec79a0a7ed7963d4b1e1",
    var rec: RecDomain = RecDomain(
        amount = "2000",
        balance = "0",
        id = "2",
        idFare = "1",
        productId = "2",
        recVal = "0"
    ),
    var route: SaleRouteDomain = SaleRouteDomain(
        id = "3",
        routeShortName = "R214"
    ),
    var sat: String = "14",
    var spe: String = "0",
    var token: String = "",
    var transfer: Boolean = false,
    var transactionType: String = "RC110",
    var vehicle: String = "SV018",
    var version: String = "3.9.55"
)

data class RecDomain(
    var amount: String = "2000",
    var balance: String = "0",
    var id: String = "2",
    var idFare: String = "1",
    var productId: String = "2",
    var recVal: String = "0"
)

data class SaleRouteDomain(
    var id: String = "3",
    var routeShortName: String = "R214"
)

fun MakeSaleRequest.isNull(): Boolean = (imei == "" || token == "")