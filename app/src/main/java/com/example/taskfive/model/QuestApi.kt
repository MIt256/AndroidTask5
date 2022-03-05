package com.example.taskfive.model

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface QuestApi {
    @GET("/api/atm?city=Гомель")//todo move to resource
    fun getAtmList(): Single<ArrayList<AtmListItem>>
    @GET("/api/infobox?city=Гомель")//todo move to resource
    fun getInfoboxList(): Single<ArrayList<AtmListItem>>
    @GET("/api/filials_info?city=Гомель")//todo move to resource
    fun getFilialList(): Single<ArrayList<AtmListItem>>
}