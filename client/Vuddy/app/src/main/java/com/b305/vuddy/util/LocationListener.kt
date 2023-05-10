package com.b305.vuddy.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.b305.vuddy.databinding.FragmentMapBinding
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class LocationListener(private val binding: FragmentMapBinding) : LocationListener {
    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    var count = 0

    fun initLocationManager(context: Context) {
        if (!::locationManager.isInitialized) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    fun setLocationListener(context: Context) {
        var isGpsSelected = false
        val minTime: Long = 100
        val minDistance = 1f
        if (!::locationListener.isInitialized) {
            locationListener = this
        }

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            var gpsLocation: Location? = null
            var networkLocation: Location? = null
            val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (isNetworkEnabled) {
                networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            if (isGPSEnabled) {
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }

            isGpsSelected = if (gpsLocation != null && networkLocation != null) {
                gpsLocation.accuracy > networkLocation.accuracy
            } else {
                gpsLocation != null
            }

            if (isGpsSelected) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    locationListener
                )
            } else {
                locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    locationListener
                )
            }
        }
    }

    override fun onLocationChanged(location: Location): Unit {
        val latitude = location.latitude.toString()
        val longitude = location.longitude.toString()
        binding.tvTime.text =
            LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        binding.tvLatitude.text = latitude
        binding.tvLongitude.text = longitude
        count++
        binding.tvCount.text = count.toString()
    }
}
