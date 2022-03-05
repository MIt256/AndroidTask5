package com.example.taskfive.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskfive.model.QuestApi
import com.example.taskfive.model.AtmListItem
import com.google.android.gms.maps.model.LatLng
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable.fromIterable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Math.sqrt
import kotlin.math.pow

class MainViewModel: ViewModel() {
    private var atmList: MutableLiveData<ArrayList<AtmListItem>> = MutableLiveData()
    private val point =  LatLng(52.425163, 31.015039)//todo to resources?

    init {
        getData()
    }

    fun getAtmList() = atmList

    private fun getData() {
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(QuestApi::class.java)

        CoroutineScope(Dispatchers.IO).launch() {
            try{
                val list = ArrayList<AtmListItem>()//todo rename
                Single.zip(
                    api.getAtmList(),
                    api.getInfoboxList(),
                    api.getFilialList(),
                    { a, i, f ->//todo rename
                        a.forEach { it.point_type = "ATM" }//todo move to constants
                        i.forEach { it.point_type = "Infobox" }
                        f.forEach { it.point_type = "Bank filial" }
                        a + i + f
                    })
                    .map { it.sortedWith(
                            compareBy {sqrt((point.latitude - it.gps_x).pow(2) + (point.longitude - it.gps_y).pow(2))   }
                        )
                    }
                    .flatMapObservable { fromIterable(it) }
                    .take(10)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        { value ->//todo it
                            list.add(value)
                            Log.e("test", value.toString())//todo delete
                        },
                        { error -> println("Error: $error") },
                        { atmList.value = list }
                    )
                //  withContext(Dispatchers.Main){ atmList.value = list}//todo delete
            } catch (ex:Exception){}//todo make ex
        }

    }

    companion object{
        val BASE_URL = "https://belarusbank.by" //todo maybe to resources
    }
}

