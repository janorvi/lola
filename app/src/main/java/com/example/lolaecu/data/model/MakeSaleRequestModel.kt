package com.example.lolaecu.data.model

import com.google.gson.annotations.SerializedName

data class MakeSaleRequestModel(
    @SerializedName("credit_enable") var creditEnable: String = "0", //estado de que si esta habilitado el credito en la tarjeta
    @SerializedName("credit_use") var creditUse: String = "0", //si usa el credito de la tarjeta en la transaccion
    @SerializedName("cur") var cur: String = "0",
    @SerializedName("dat") var date: String = "1695180099", //fecha en timestamp en el que se envia la transaccion debe ser horario de colombia
    @SerializedName("drv") var driver: String = "265", //conductor
    @SerializedName("employee_full_name") var employeeFullName: String = "TCMR TECN2", //nombre del empleado conductor
    @SerializedName("hw") var hw: String = "1111",
    @SerializedName("ime") var imei: String = "", //codigo serial del validador
    @SerializedName("lat") var latitude: String = "4.69005",
    @SerializedName("lon") var longitude: String = "-74.0708",
    @SerializedName("offline") var offline: String = "0", //si el validador tiene una transaccion que habilito si saber si tenia o no dinero la tarjeta
    @SerializedName("plot_id") var plotId: String = "b55d88d11eee9799995cec79a0a7ed7963d4b1e1", //identificador unico de la transaccion
    @SerializedName("rec") var rec: Rec = Rec( //objeto con campos necesarios para la recaudo de dinero
        amount = "2000",
        balance = "0",
        id = "2",
        idFare = "1",
        productId = "2",
        recVal = "0"
    ),
    @SerializedName("route") var route: SaleRoute = SaleRoute( //objeto con los campos de la ruta actual
        id = "3",
        routeShortName = "R214"
    ),
    @SerializedName("sat") var sat: String = "14",
    @SerializedName("spe") var spe: String = "0",
    @SerializedName("token") var token: String = "", //hexadecimal de la tajeta o valor del qr en caso que sea un qr
    @SerializedName("transfer") var transfer: Boolean = false, //si es un trasbordo
    @SerializedName("typ") var transactionType: String = "RC110", //'tipo de transaccion tarjeta o qr
    @SerializedName("veh") var vehicle: String = "SV018", //numero del vehiculo
    @SerializedName("ver") var version: String = "3.9.55" //version del software del validador
)

data class Rec(
    @SerializedName("amount") var amount: String = "2000",
    @SerializedName("balance") var balance: String = "0",
    @SerializedName("id") var id: String = "2",
    @SerializedName("id_fare") var idFare: String = "1",
    @SerializedName("product_id") var productId: String = "2",
    @SerializedName("val") var recVal: String = "0"
)

data class SaleRoute(
    @SerializedName("id") var id: String = "3",
    @SerializedName("route_short_name") var routeShortName: String = "R214"
)