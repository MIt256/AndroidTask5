package com.example.taskfive

import com.example.taskfive.Constants.Companion.URL_BASE
import com.example.taskfive.model.QuestApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitObj {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(URL_BASE)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    private val api by lazy {
        retrofit.create(QuestApi::class.java)
    }

    fun getAtmList() =
        api.getAtmList()

    fun getInfoboxList() =
       api.getInfoboxList()

    fun getFilialList() =
        api.getFilialList()

}

