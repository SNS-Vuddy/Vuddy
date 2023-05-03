//package com.b305.buddy.service
//
//import android.app.*
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.icu.util.Calendar
//import android.os.Build
//import android.os.Handler
//import android.os.IBinder
//import android.util.Log
//import androidx.annotation.RequiresApi
//import com.b305.buddy.util.PreferenceUnit
//
//class TempServiceA : Service() {
//    override fun onCreate() {
//        super.onCreate()
//
//        PreferenceUnit.init(applicationContext)
//        sendLogA()
//    }
//
//    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
//        Log.d("TempServiceA: onStartCommand", "onStartCommand in TempServiceA")
//
//        if (intent?.getStringExtra("type") == "B") {
//            startService(Intent(this, TempServiceB::class.java))
//        }
//
//        if (PreferenceUnit.getInstance().preIsRealStop) {
//            PreferenceUnit.getInstance().preIsRealStop = false
//            Log.d("TempServiceA: onStartCommand", "change preIsRealStop true -> false")
//        }
//
//        return START_STICKY
//    }
//
//    override fun onBind(intent: Intent?): IBinder? {
//        Log.d("TempServiceA: onBind", "onBind in TempServiceA")
//        return null
//    }
//
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Log.d("TempServiceA: onDestroy", "onDestroy in TempServiceA")
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            if (!PreferenceUnit.getInstance().preIsRealStop) {
//                callAlarmManager()
//            }
//        }
//    }
//
//    // 로직
//    private fun sendLogA() {
//        val handler = Handler()
//        val runnable = object : Runnable {
//            override fun run() {
//                Log.d("TempServiceA: sendLogA", "sendLogA in TempServiceA")
//                handler.postDelayed(this, 3000)
//            }
//        }
//        handler.postDelayed(runnable, 3000)
//    }
//
//    @RequiresApi(Build.VERSION_CODES.N)
//    private fun callAlarmManager() {
//        val calendar = Calendar.getInstance()
//        calendar.timeInMillis = System.currentTimeMillis()
//        calendar.add(Calendar.SECOND, 3)
//
//        val intent = Intent(this, AlarmReceiver::class.java)
//        val sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = sender
//    }
//
//    companion object {
//        private const val CHANNEL_ID = "ID_ServiceA"
//        private const val CHANNEL_NAME = "NAME_ServiceA"
//    }
//}
//
//class AlarmReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context?, intent: Intent?) {
//        val mintent: Intent
//
//        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        //    mintent
//        //} else {
//        //
//        //}
//    }
//}