package com.b305.vuddy.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.b305.vuddy.databinding.ActivityTestBinding
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class TestActivity : AppCompatActivity(), LocationListener {
    lateinit var locationManager: LocationManager
    lateinit var locationListener: LocationListener
    lateinit var binding: ActivityTestBinding
    var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getMyLocation()
        setLocationListener()
    }

    private fun getMyLocation() {
        // location manager 초기화
        if (!::locationManager.isInitialized) {
            locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    private fun setLocationListener() {
        val minTime: Long = 100
        val minDistance = 10f
        if (!::locationListener.isInitialized) {
            locationListener = this
        }
        with(locationManager) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                minTime, minDistance, locationListener
            )

//            requestLocationUpdates(
//                LocationManager.NETWORK_PROVIDER,
//                minTime, minDistance, locationListener
//            )
        }
    }

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude.toString()
        val longitude = location.longitude.toString()
        Log.d("TestActivity", "$latitude, $longitude")

        val localDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
        val localDateTimeFormat = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        count++
        binding.tvCount.text = count.toString()
        binding.tvTime.text = localDateTimeFormat
        binding.tvLatitude.text = latitude
        binding.tvLongitude.text = longitude
    }
}
