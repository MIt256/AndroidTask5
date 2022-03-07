package com.example.taskfive.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.taskfive.Constants.Companion.ERROR
import com.example.taskfive.Constants.Companion.POINT_CENTRAL
import com.example.taskfive.Constants.Companion.TYPE_ATM
import com.example.taskfive.Constants.Companion.TYPE_BANK
import com.example.taskfive.Constants.Companion.TYPE_INFOBOX
import com.example.taskfive.model.MapPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable.fromIterable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.taskfive.RetrofitObj
import java.lang.Math.sqrt
import kotlin.math.pow

class MainViewModel: ViewModel() {
    private var mapPointList: MutableLiveData<ArrayList<MapPoint>> = MutableLiveData()

    fun isListEmpty():Boolean=
        mapPointList.value.isNullOrEmpty()

    fun getPointList() = mapPointList

     fun getData() {
        val api = RetrofitObj
        CoroutineScope(Dispatchers.IO).launch() {
            try{
                val pointList = ArrayList<MapPoint>()
                Single.zip(
                    api.getAtmList(),
                    api.getInfoboxList(),
                    api.getFilialList(),
                    { atm, infobox, filial ->
                        atm.forEach { it.pointType = TYPE_ATM }
                        infobox.forEach { it.pointType = TYPE_INFOBOX }
                        filial.forEach { it.pointType = TYPE_BANK }
                        atm + infobox + filial
                    })
                    .map { it.sortedWith(
                            compareBy {sqrt((POINT_CENTRAL.latitude - it.gps_x).pow(2) + (POINT_CENTRAL.longitude - it.gps_y).pow(2))   }
                        )
                    }
                    .flatMapObservable { fromIterable(it) }
                    .take(10)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {pointList.add(it)},
                        {Log.e(ERROR," $it")},
                        {mapPointList.value = pointList}
                    )
            } catch (ex:Exception){Log.e(ERROR," $ex")}
        }

    }
}

