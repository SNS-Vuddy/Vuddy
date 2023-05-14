package com.b305.vuddy.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat

class LocationProvider(val context: Context) : LocationListener {
    private var location: Location? = null
    private var locationManager: LocationManager? = null

    init {
        getLocation()
    }

     fun getLocation(): Location? {
        try {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

            var gpsLocation: Location? = null
            var networkLocation: Location? = null

            // 활성화 확인
            val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGPSEnabled && !isNetworkEnabled) { // Case 3
                return null
            } else {
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return null
                }

                if (isNetworkEnabled) {
                    networkLocation = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                }

                if (isGPSEnabled) {
                    gpsLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                }

                // Case 1
                if (gpsLocation != null && networkLocation != null) {
                    if (gpsLocation.accuracy > networkLocation.accuracy) {
                        location = gpsLocation
                    } else {

                        //Todo 모바일 환경에서는 아래 주석 해제하고 사용
//                        location = networkLocation

                        //Todo 에뮬레이터 환경에서는 아래 주석 해제하고 사용
                        location = gpsLocation
                    }
                } else { // Case 2
                    location = gpsLocation ?: networkLocation
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return location
    }

    fun getLocationLatitude(): Double? {
        return location?.latitude
    }

    fun getLocationLongitude(): Double? {
        return location?.longitude
    }

    override fun onLocationChanged(location: Location) {
        TODO("Not yet implemented")
    }
}
