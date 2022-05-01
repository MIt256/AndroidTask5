package com.example.taskfive

import com.google.android.gms.maps.model.LatLng

class Constants {
    companion object {
        const val URL_ATM = "/api/atm?city=Гомель"
        const val URL_INFOBOX ="/api/infobox?city=Гомель"
        const val URL_BANK = "/api/filials_info?city=Гомель"
        const val URL_BASE = "https://belarusbank.by"

        const val ERROR = "Error"

        const val TYPE_ATM = "Банкомат"
        const val TYPE_INFOBOX = "Инфокиоск"
        const val TYPE_BANK = "Отделение банка"

        val POINT_CENTRAL =  LatLng(52.425163, 31.015039)

    }
}