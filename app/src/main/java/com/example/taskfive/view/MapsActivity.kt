package com.example.taskfive.view

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.taskfive.Constants.Companion.POINT_CENTRAL
import com.example.taskfive.R
import com.example.taskfive.vm.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.taskfive.databinding.ActivityMapsBinding
import com.example.taskfive.model.MapPoint
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private val binding by lazy { ActivityMapsBinding.inflate(layoutInflater) }
    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch() {
            if (mainViewModel.isListEmpty()) {
                while (!checkConnectivity())
                    delay(5000)
                mainViewModel.getData()
            }
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mainViewModel.getPointList().observe(this, {
            it?.let {
                updateMap(it)
            }
        })

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(POINT_CENTRAL,13f))
    }

    private fun updateMap(pointList: ArrayList<MapPoint>) {
       pointList.forEach{
            mMap.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(it.gps_x, it.gps_y)
                    )
                    .title(it.pointType)
                    .snippet(it.city_type +" "+ it.city+", "+
                        it.address_type+ " "+it.address + " "+ it.house)
                    .icon(BitmapDescriptorFactory.defaultMarker(265F))
            )
        }
    }

    private fun checkConnectivity():Boolean{
        val connectManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectManager.activeNetwork != null) return true
        return false
    }

}