package com.b305.vuddy.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.b305.vuddy.R
import com.b305.vuddy.activity.MainActivity
import com.b305.vuddy.util.ChatSocket
import com.b305.vuddy.util.LocationSocket
import com.b305.vuddy.util.NO_LOCATION_NOTIFICATION_ID

/**
 * 내 위치 공유하지 않는 이모탈 서비스
 * 2소켓 재연결
 */
class ImmortalChatService : LifecycleService() {
    private lateinit var chatSocket: ChatSocket
    private lateinit var locationSocket: LocationSocket

    override fun onCreate() {
        super.onCreate()
        createNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (!::chatSocket.isInitialized) {
            chatSocket.connection()
        }
        if (!::locationSocket.isInitialized) {
            locationSocket.connection()
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        chatSocket.disconnect()
        locationSocket.disconnect()
    }

    private fun createNotification() {
        val builder = NotificationCompat.Builder(this, "default")
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setContentTitle("Vuddy")
        builder.setContentText("Vuddy가 실행 중입니다.")
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
        notificationManager.notify(NO_LOCATION_NOTIFICATION_ID, builder.build()) // id : 정의해야하는 각 알림의 고유한 int값
        val notification = builder.build()
        startForeground(NO_LOCATION_NOTIFICATION_ID, notification)
    }
}
