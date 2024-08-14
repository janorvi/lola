package com.example.lolaecu.data.model

import com.google.gson.annotations.SerializedName


data class CashSaleRequestModel(
    @SerializedName("data") var cashData: CashData? = CashData()
)

data class Cou(
    @SerializedName("in") var cashIn: String? = "3962",
    @SerializedName("out") var out: String? = "21773",
    @SerializedName("enc") var enc: String? = "1",
    @SerializedName("oth") var oth: String? = "754230",
    @SerializedName("tur") var tur: String? = "3962",
    @SerializedName("valid") var valid: Boolean? = true
)

data class ModeA(
    @SerializedName("distance") var distance: String? = "0",
    @SerializedName("route_id") var routeId: String? = "0",
    @SerializedName("service_id") var serviceId: String? = "2434",
    @SerializedName("stop_id") var stopId: String? = "0"
)

data class CashRoute(
    @SerializedName("adv") var adv: String? = "0",
    @SerializedName("b_sp") var bSp: String? = "0",
    @SerializedName("a_sp") var aSp: String? = "0",
    @SerializedName("ds_id") var dsId: String? = "247890",
    @SerializedName("mode") var mode: String? = "B",
    @SerializedName("mode_a") var modeA: ModeA? = ModeA(),
    @SerializedName("n_sp") var nSp: String? =  "1",
    @SerializedName("pso") var pso: String? = "0",
    @SerializedName("idro") var idro: String? = "0",
    @SerializedName("ro_id") var roId: String? =  "1",
    @SerializedName("state") var state: String? =  "1",
    @SerializedName("ser_id") var serId: String? = "2434",
    @SerializedName("stsi") var stsi: String? = "1"
)

data class CashRec(
    @SerializedName("qty") var qty: String? = "1",
    @SerializedName("tot") var tot: String? = "2400",
    @SerializedName("typ") var typ: String? = "3"
)

data class CashData(
    @SerializedName("ver") var ver: String? = "3.7.9",
    @SerializedName("ime") var ime: String? = "868808038416084",
    @SerializedName("typ") var typ: String? = "RC101",
    @SerializedName("dat") var dat: String? = "1698759931349",
    @SerializedName("veh") var veh: String? = "862708044045230",
    @SerializedName("lat") var lat: String? = "10.45014476776123",
    @SerializedName("lon") var lon: String? = "-73.26789093017578",
    @SerializedName("spe") var spe: String? = "0.02",
    @SerializedName("cur") var cur: String? = "63.2",
    @SerializedName("sat") var sat: String? = "12",
    @SerializedName("hw") var hw: String? = "01011011",
    @SerializedName("odo") var odo: String? = "20368918.00",
    @SerializedName("cou") var cou: Cou? = Cou(),
    @SerializedName("route") var cashRoute: CashRoute? = CashRoute(),
    @SerializedName("rec") var cashRec: CashRec? = CashRec(),
    @SerializedName("drv") var drv: String? = "170",
    @SerializedName("clo") var clo: String? = null,
    @SerializedName("status") var status: Int? = 0
)