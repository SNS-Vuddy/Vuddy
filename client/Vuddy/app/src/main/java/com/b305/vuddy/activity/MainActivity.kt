package com.b305.vuddy.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.b305.vuddy.R
import com.b305.vuddy.service.TestService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        connectAndSendToLocationSocket()
        startService(Intent(this, TestService::class.java))
    }

//    private fun connectAndSendToLocationSocket() {
//        val locationSocket = LocationSocket(this)
//        val locationProvider = LocationProvider(this)
//
//        locationSocket.connection()
//        val handler = Handler()
//        val runnable = object : Runnable {
//            override fun run() {
//                val latitude = locationProvider.getLocationLatitude().toString()
//                val longitude = locationProvider.getLocationLongitude().toString()
//                locationSocket.sendLocation(latitude, longitude)
//                handler.postDelayed(this, 1000)
//            }
//        }
//        handler.post(runnable)
//    }
}
