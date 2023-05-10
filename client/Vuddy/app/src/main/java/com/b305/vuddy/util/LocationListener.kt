//package com.b305.vuddy.util
//
//import android.Manifest
//import android.content.Context
//import android.content.pm.PackageManager
//import android.location.Location
//import android.location.LocationListener
//import android.location.LocationManager
//import androidx.core.app.ActivityCompat
//import com.b305.vuddy.databinding.FragmentMapBinding
//import java.time.LocalDateTime
//import java.time.ZoneId
//import java.time.format.DateTimeFormatter
//
//open class LocationListener(private val binding: FragmentMapBinding) : LocationListener {
//    lateinit var locationManager: LocationManager
//    lateinit var locationListener: LocationListener
//    var count = 0
//
//    fun initLocationManager(context: Context) {
//        if (!::locationManager.isInitialized) {
//            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//        }
//    }
//
//    fun setLocationListener(context: Context) {
//        var isGpsSelected = false
//        val minTime = 100L
//        val minDistance = 1f
//        lateinit var locationListener: LocationListener
//
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED ||
//            ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            val gpsProvider = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
//            val networkProvider = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
//            val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
//            val networkLocation =
//                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
//
//            isGpsSelected = gpsLocation?.let { gps ->
//                networkLocation?.let { network ->
//                    gps.accuracy > network.accuracy
//                } ?: true
//            } ?: false
//
//            val provider = if (isGpsSelected) LocationManager.GPS_PROVIDER else LocationManager.NETWORK_PROVIDER
//            locationListener = this
//
//            locationManager.requestLocationUpdates(
//                provider,
//                minTime,
//                minDistance,
//                locationListener
//            )
//        }
//    }
//
//    override fun onLocationChanged(location: Location): Unit {
//        val latitude = location.latitude.toString()
//        val longitude = location.longitude.toString()
//        val localDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
//            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
//
//        binding.tvTime.text = localDateTime
//        count++
//        binding.tvCount.text = count.toString()
//        binding.tvLatitude.text = latitude
//        binding.tvLongitude.text = longitude
//
//        mMap
//    }
//}
