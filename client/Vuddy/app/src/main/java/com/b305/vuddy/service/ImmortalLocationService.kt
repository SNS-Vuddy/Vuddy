//package com.b305.vuddy.service
//
//import android.app.NotificationChannel
//import android.app.NotificationManager
//import android.app.PendingIntent
//import android.app.Service
//import android.content.Intent
//import android.graphics.Color
//import android.os.Handler
//import android.os.IBinder
//import android.util.Log
//import androidx.core.app.NotificationCompat
//import com.b305.vuddy.R
//import com.b305.vuddy.activity.MainActivity
//import com.b305.vuddy.util.LocationProvider
//import com.b305.vuddy.util.LocationSocket
//
//class ImmortalLocationService() : Service() {
//    var handler = Handler()
//    var runnable: Runnable? = null
//    var locationSocket: LocationSocket? = null
//    var isHandlerRunning = false
//
//    override fun onBind(intent: Intent): IBinder? {
//        throw UnsupportedOperationException("Not yet implemented")
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        createNotification()
//    }
//
//    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        if (isHandlerRunning) {
//            Log.d("ImmortalLocationService", "****isHandlerRunning****")
//            isHandlerRunning = false
//            handler.removeCallbacks(runnable as Runnable)
//        }
//        val locationProvider = LocationProvider(this)
//        locationSocket = LocationSocket(this)
//        locationSocket!!.connection()
//        runnable = object : Runnable {
//            override fun run() {
//                val latitude = locationProvider.getLocationLatitude().toString()
//                val longitude = locationProvider.getLocationLongitude().toString()
//                locationSocket!!.sendLocation(latitude, longitude)
//                handler.postDelayed(this, 5000)
//            }
//        }
//        isHandlerRunning = true
//        handler.post(runnable as Runnable)
//        return super.onStartCommand(intent, flags, startId)
//    }
//
//    private fun createNotification() {
//        val builder = NotificationCompat.Builder(this, "default")
//        builder.setSmallIcon(R.mipmap.ic_launcher)
//        builder.setContentTitle("Vuddy")
//        builder.setContentText("친구들에게 내 위치를 공유중입니다.")
//        builder.color = Color.RED
//        val notificationIntent = Intent(this, MainActivity::class.java)
//        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE)
//        builder.setContentIntent(pendingIntent)
//
//        // 알림 표시
//        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        notificationManager.createNotificationChannel(
//            NotificationChannel(
//                "default",
//                "기본 채널",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//        )
//        notificationManager.notify(NOTI_ID, builder.build()) // id : 정의해야하는 각 알림의 고유한 int값
//        val notification = builder.build()
//        startForeground(NOTI_ID, notification)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        isHandlerRunning = false
//        handler.removeCallbacks(runnable as Runnable)
//        locationSocket!!.disconnect()
//    }
//
//    companion object {
//        private const val TAG = "MyServiceTag"
//        private const val NOTI_ID = 1
//    }
//}
