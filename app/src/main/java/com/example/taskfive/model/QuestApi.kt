package com.example.taskfive.model

import com.example.taskfive.Constants.Companion.URL_ATM
import com.example.taskfive.Constants.Companion.URL_BANK
import com.example.taskfive.Constants.Companion.URL_INFOBOX
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface QuestApi {
    @GET(URL_ATM)
    fun getAtmList(): Single<ArrayList<MapPoint>>
    @GET(URL_INFOBOX)
    fun getInfoboxList(): Single<ArrayList<MapPoint>>
    @GET(URL_BANK)
    fun getFilialList(): Single<ArrayList<MapPoint>>
}