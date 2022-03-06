package com.example.taskfive.model

import com.google.gson.annotations.SerializedName

data class MapPoint(
    @SerializedName("address",alternate= ["street"])
    val address: String,
    @SerializedName("address_type",alternate= ["street_type"])
    val address_type: String,
    @SerializedName("city",alternate= ["name"])
    val city: String,
    @SerializedName("city_type",alternate= ["name_type"])
    val city_type: String,
    @SerializedName("gps_x",alternate= ["GPS_X"])
    val gps_x: Double,
    @SerializedName("gps_y",alternate= ["GPS_Y"])
    val gps_y: Double,
    @SerializedName("house",alternate= ["home_number"])
    val house: String
){
    var pointType: String=""
}
