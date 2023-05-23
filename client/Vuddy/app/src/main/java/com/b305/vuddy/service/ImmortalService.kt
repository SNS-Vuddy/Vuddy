package com.b305.vuddy.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.b305.vuddy.R
import com.b305.vuddy.view.activity.MainActivity
import com.b305.vuddy.model.UserLocation
import com.b305.vuddy.util.ChatSocket
import com.b305.vuddy.util.FASTEST_LOCATION_UPDATE_INTERVAL
import com.b305.vuddy.util.LOCATION_UPDATE_INTERVAL
import com.b305.vuddy.util.location.LocationSocket
import com.b305.vuddy.util.NOTI_ID
import com.b305.vuddy.util.SharedManager
import com.b305.vuddy.util.location.LocationUtility
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.greenrobot.eventbus.EventBus

@Suppress("DEPRECATION")
class ImmortalService : LifecycleService() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationSocket: LocationSocket
    private lateinit var chatSocket: ChatSocket
    private lateinit var sharedManager: SharedManager

    // 선언
    companion object {
        val isTracking = MutableLiveData<Boolean>()
    }

    // 초기화
    private fun postInitialValues() {
        isTracking.postValue(false)
    }

    inner class LocalBinder : Binder() {
        fun getService(): ImmortalService = this@ImmortalService
    }

    private val binder = LocalBinder()

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)
        return binder
    }

    override fun onCreate() {
        super.onCreate()

        createNotification()
        postInitialValues()

        isTracking.observe(this) {
            updateLocation()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        isTracking.postValue(true)
        Log.d("ImmortalService", "****onStartCommand****")

        if (!::chatSocket.isInitialized) {
            chatSocket = ChatSocket(applicationContext)
            chatSocket.connection()
        }

        return START_NOT_STICKY
    }


    // 요청
    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        Log.d("ImmortalService", "****updateLocation****")
        if (!::fusedLocationProviderClient.isInitialized) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        }
        if (LocationUtility.hasLocationPermissions(this)) {
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

            if (!::locationSocket.isInitialized) {
                locationSocket = LocationSocket(applicationContext)
                locationSocket.connection()
                Log.d("ImmortalService", "****locationSocket****")
            }

            if (!::sharedManager.isInitialized) {
                sharedManager = SharedManager(applicationContext)
            }
            val user = sharedManager.getCurrentUser()
            val nickname = user.nickname
            val location = result.locations.last()
            val latitude = location.latitude.toString()
            val longitude = location.longitude.toString()
            val profileImgUrl = user.profileImgUrl
            val statusImgUrl = user.statusImgUrl

            val userLocation = UserLocation(nickname, latitude, longitude, statusImgUrl, profileImgUrl)
            locationSocket.sendLocation(userLocation)
            EventBus.getDefault().post(userLocation)
        }
    }

    private fun createNotification() {
        val builder = NotificationCompat.Builder(this, "default")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Vuddy")
        builder.setContentText("친구들에게 내 위치를 공유중입니다.")
        builder.color = Color.RED
        val notificationIntent = Intent(this, MainActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE)
        builder.setContentIntent(pendingIntent)

        // 알림 표시
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            NotificationChannel(
                "default",
                "기본 채널",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
        notificationManager.notify(NOTI_ID, builder.build()) // id : 정의해야하는 각 알림의 고유한 int값
        val notification = builder.build()
        startForeground(NOTI_ID, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocationUpdates()
        locationSocket.disconnect()
        chatSocket.disconnect()
    }

    private fun stopLocationUpdates() {
        if (::fusedLocationProviderClient.isInitialized) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                .addOnCompleteListener {
                    Log.d("ImmortalService", "****stopLocationUpdates****")
                }
        }
    }

    fun getChatSocket(): ChatSocket {
        return chatSocket
    }
}

