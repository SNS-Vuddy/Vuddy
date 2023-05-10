package com.b305.vuddy.service

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.b305.vuddy.model.LocationEvent
import com.b305.vuddy.model.UserLocation
import com.b305.vuddy.util.FASTEST_LOCATION_UPDATE_INTERVAL
import com.b305.vuddy.util.LOCATION_UPDATE_INTERVAL
import com.b305.vuddy.util.SharedManager
import com.b305.vuddy.util.TrackingUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.greenrobot.eventbus.EventBus

class TestService : LifecycleService() {
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    //    lateinit var baseNotificationBuilder: NotificationCompat.Builder
//    lateinit var currentNotificationBuilder: NotificationCompat.Builder
    private val sharedManager: SharedManager by lazy { SharedManager(applicationContext) }

    // 선언
    companion object {
        val isTracking = MutableLiveData<Boolean>()
    }

    // 초기화
    private fun postInitialValues() {
        isTracking.postValue(false)
    }

    override fun onCreate() {
        super.onCreate()
//        currentNotificationBuilder = baseNotificationBuilder
        postInitialValues()

        isTracking.observe(this) {
            updateLocation(it)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isTracking.postValue(true)
        Log.d("****", "onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    // 요청
    @SuppressLint("MissingPermission")
    private fun updateLocation(isTracking: Boolean) {
        Log.d("****", "updateLocation")
        if (!::fusedLocationProviderClient.isInitialized) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        }
        if (TrackingUtility.hasLocationPermissions(this)) {
            val request = LocationRequest.create().apply {
                interval = LOCATION_UPDATE_INTERVAL // 위치 업데이트 주기
                fastestInterval = FASTEST_LOCATION_UPDATE_INTERVAL // 가장 빠른 위치 업데이트 주기
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 배터리소모를 고려하지 않으며 정확도를 최우선으로 고려
                maxWaitTime = LOCATION_UPDATE_INTERVAL // 최대 대기시간
            }
            fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())
        }
    }

    // 수신
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            super.onLocationResult(result)
            val location = result.locations.last()
            Log.d("****", "${location.latitude}, ${location.longitude}")

            val userLocation = UserLocation()
            val latitude = location.latitude.toString()
            val longitude = location.longitude.toString()
            userLocation.lat = latitude
            userLocation.lng = longitude
            EventBus.getDefault().post(LocationEvent(true, userLocation))
        }
    }
}
