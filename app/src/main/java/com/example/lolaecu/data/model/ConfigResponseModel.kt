package com.example.lolaecu.data.model

import com.google.gson.annotations.SerializedName

data class ConfigResponseModel(
    @SerializedName("session") var session: String = "",
    @SerializedName("tsession") var tsession: String = "",
    @SerializedName("vsession") var vsession: String = "",
    @SerializedName("config_frequency") var configFrequency: Int = 0,
    @SerializedName("assign") var assign: Assign = Assign(),
    @SerializedName("credit_enable_card") var creditEnableCard: String = "",
    @SerializedName("host") var host: String = "",
    @SerializedName("token") var token: String = "",
    @SerializedName("timeout") var timeout: Timeout = Timeout(),
    @SerializedName("resend") var resend: Boolean = false,
    @SerializedName("reverse") var reverse: Boolean = false,
    @SerializedName("updateFirmware") var updateFirmware: String = "",
    @SerializedName("mode") var mode: String = "",
    @SerializedName("rb_card_reader") var rbCardReader: Int = 0,
    @SerializedName("hash") var hash: String = "",
    @SerializedName("tit") val timeForTranshipment: Int = 0,
    @SerializedName("ctype") val ctype: Int = 0
)

data class Route(
    @SerializedName("route_short_name") var routeShortName: String = "",
    @SerializedName("imei") var imei: String = "",
    @SerializedName("vehicle_internal_num") var vehicleInternalNum: String = "",
    @SerializedName("employee_id") var employeeId: Int = 0,
    @SerializedName("employee_full_name") var employeeFullName: String = "",
    @SerializedName("route_id") var routeId: String = "",
    @SerializedName("type") var type: String = ""
)

data class Transhipments(
    @SerializedName("source_path") var sourcePath: Int = 0,
    @SerializedName("routes_destinations") var routesDestinations: List<Int> = listOf(),
    @SerializedName("source_name") var sourceName: String = ""
)

data class Fares(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("name") var name: String = "",
    @SerializedName("is_fixed") var isFixed: Boolean = false,
    @SerializedName("value") var value: String = "",
    @SerializedName("days") var days: List<String> = listOf(),
    @SerializedName("init_hour") var initHour: String = "",
    @SerializedName("finish_hour") var finishHour: String = "",
    @SerializedName("cost_per_kilometer") var costPerKilometer: String = "",
    @SerializedName("transshipment_applies") var transshipmentApplies: Boolean = false,
    @SerializedName("transshipment_time") var transshipmentTime: Int = 0,
    @SerializedName("transshipment_pay") var transshipmentPay: Int = 0,
    @SerializedName("transshipments") var transhipments: List<Transhipments> = listOf(),
    @SerializedName("transshipment_quantity") var transhipmentQuantity: Int = 0,
    @SerializedName("vehicle_typology") var vehicleTypology: String = "",
    @SerializedName("product_id") var productId: Int = 0,
    @SerializedName("is_active") var isActive: Boolean = false,
    @SerializedName("is_default") var isDefault: Boolean = false,
    @SerializedName("is_qr") var isQr: Boolean = false,
    @SerializedName("fare_stops") var fareStops: List<FareStop> = listOf()
)

data class Fare(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("product_id") var productId: Int = 0,
    @SerializedName("name") var name: String = "",
    @SerializedName("product_status_id") var productStatusId: Int = 0,
    @SerializedName("fares") var fares: List<Fares> = listOf()
)

data class Assign(
    @SerializedName("route") var route: Route = Route(),
    @SerializedName("fare") var fare: List<Fare> = listOf()
)

data class Timeout(
    @SerializedName("config_frequency") var configFrequency: Int = 0,
    @SerializedName("turnstyle") var turnstyle: String = "",
    @SerializedName("transaction") var transaction: String = "",
    @SerializedName("afterpay") var afterpay: String = "",
    @SerializedName("sync") var sync: String = "",
    @SerializedName("init") var init: String = ""
)

data class FareStop(
    @SerializedName("id") var id: Int = 0,
    @SerializedName("fare_id") var fareId: Int = 0,
    @SerializedName("route_id") var routeId: Int = 0,
    @SerializedName("stop_id") var stopId: Int = 0,
    @SerializedName("route") var route: FareStopRoute = FareStopRoute(),
    @SerializedName("stop") var stop: Stop = Stop()
)

data class FareStopRoute(
    @SerializedName("short_name") var shortName: String = "",
    @SerializedName("id") var id: Int = 0
)

data class Geodata(
    @SerializedName("longitude") var longitude: String = "",
    @SerializedName("latitude") var latitude: String = "",
    @SerializedName("radius") var radius: String = ""
)

data class Stop(
    @SerializedName("short_name") var shortName: String = "",
    @SerializedName("id") var id: Int = 0,
    @SerializedName("geodata") var geodata: ArrayList<Geodata> = arrayListOf()
)