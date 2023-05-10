package com.b305.vuddy.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.b305.vuddy.R
import com.b305.vuddy.activity.MainActivity
import com.b305.vuddy.fragment.MapFragment
import com.b305.vuddy.model.LocationEvent
import com.b305.vuddy.model.UserLocation
import com.b305.vuddy.util.LocationProvider
import com.b305.vuddy.util.LocationSocket
import org.greenrobot.eventbus.EventBus

class ImmortalLocationService() : Service(), LocationListener {
    private lateinit var locationProvider: LocationProvider
    private lateinit var locationSocket: LocationSocket
    private lateinit var locationManager: LocationManager

    override fun onBind(intent: Intent): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        createNotification()
        initLocationManager(this)
        setLocationListener(this)
    }

    private fun initLocationManager(context: Context) {
        if (!::locationManager.isInitialized) {
            locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

    private fun setLocationListener(context: Context) {
        val isGpsSelected: Boolean
        val minTime = 100L
        val minDistance = 10f
        lateinit var locationListener: LocationListener
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val networkLocation =
                locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

            isGpsSelected = gpsLocation?.let { gps ->
                networkLocation?.let { network ->
                    gps.accuracy > network.accuracy
                } ?: true
            } ?: false

            val provider = if (isGpsSelected) LocationManager.GPS_PROVIDER else LocationManager.NETWORK_PROVIDER
            locationListener = this

            locationManager.requestLocationUpdates(
                provider,
                minTime,
                minDistance,
                locationListener
            )
        }
    }

    override fun onLocationChanged(location: Location) {
        Log.d("ImmortalLocationService", "****onLocationChanged****")
        if (!::locationProvider.isInitialized) {
            locationProvider = LocationProvider(this)
        }
        if (!::locationSocket.isInitialized) {
            locationSocket = LocationSocket(this)
            locationSocket.connection()
        }
        val latitude = locationProvider.getLocationLatitude().toString()
        val longitude = locationProvider.getLocationLongitude().toString()
        locationSocket.sendLocation(latitude, longitude)

        val userLocation = UserLocation()
        userLocation.lat = latitude
        userLocation.lng = longitude
        EventBus.getDefault().post(LocationEvent(true, userLocation))
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
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
        locationSocket.disconnect()
    }

    companion object {
        private const val NOTI_ID = 1
    }
}
