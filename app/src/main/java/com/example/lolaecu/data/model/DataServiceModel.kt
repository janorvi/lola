package com.example.lolaecu.data.model

import com.google.gson.annotations.SerializedName

data class DataServiceModel(
    @SerializedName("id_route") var idRoute: Int? = null,
    @SerializedName("name_long") var routeName: String? = null,
    @SerializedName("id") var idService: Int? = null,
    @SerializedName("id") var idDetailService: Int? = null,
    @SerializedName("pi") var piName: String? = null,
    @SerializedName("pf") var pfName: String? = null,
    @SerializedName("hi") var hi: String? = null,//start_time->detail_services
    @SerializedName("hf") var hf: String? = null,//end_time->detail_services
    @SerializedName("dis") var dis: String? = null,
    @SerializedName("time") var time: String? = null,
    @SerializedName("stops") var stops: String? = null
)
