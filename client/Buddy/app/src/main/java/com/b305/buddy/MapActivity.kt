package com.b305.buddy

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.b305.buddy.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    
    lateinit var binding: ActivityMapBinding
    private var mMap: GoogleMap? = null
    
    var currentLat: Double = 37.4979769 // default: 강남역
    var currentLng: Double = 127.027729
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment?.getMapAsync(this)
    }
    
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d("MapActivity", "onMapReady")
        
        val locationProvider = LocationProvider(this@MapActivity)
        val currentLat = locationProvider.getLocationLatitude()
        val currentLng = locationProvider.getLocationLongitude()
        
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(currentLat!!, currentLng!!), 16f))
    }
}