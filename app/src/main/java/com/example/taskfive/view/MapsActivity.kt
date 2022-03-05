package com.example.taskfive.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.taskfive.R
import com.example.taskfive.vm.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.taskfive.databinding.ActivityMapsBinding
import com.example.taskfive.model.AtmListItem
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap//todo maybe lazy?
    private lateinit var binding: ActivityMapsBinding

    private val mainViewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mainViewModel.getAtmList().observe(this, {
            it?.let {
                if (::mMap.isInitialized)
                    updateMap(it)
            }
        })
        //todo fix bounds
        val adelaideBounds = LatLngBounds(
            LatLng(52.337240, 30.834963),  // SW bounds
            LatLng(52.557113, 31.101576) // NE bounds
        )
        mMap.setLatLngBoundsForCameraTarget(adelaideBounds)

        mMap.setMinZoomPreference(11.0f)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(52.424169, 31.014267)))
    }

    private fun updateMap(atmList: ArrayList<AtmListItem>) {
        for (atm in atmList) {//todo rename
            mMap.addMarker(
                MarkerOptions()
                    .position(
                        LatLng(
                            atm.gps_x,
                            atm.gps_y
                        )
                    )
                    .title(atm.point_type)//todo fix text
                    .snippet(atm.city_type +" "+ atm.city+", "+
                        atm.address_type+ " "+atm.address + " "+ atm.house)
                    .icon(BitmapDescriptorFactory.defaultMarker(265F))
            )
        }
    }

}