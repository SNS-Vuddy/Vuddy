package com.b305.vuddy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.b305.vuddy.R
import com.b305.vuddy.util.LocationProvider
import com.b305.vuddy.util.LocationSocket

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectAndSendToLocationSocket()
    }

    private fun connectAndSendToLocationSocket() {
        val locationSocket = LocationSocket(this)
        val locationProvider = LocationProvider(this)

        locationSocket.connection()
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                val latitude = locationProvider.getLocationLatitude().toString()
                val longitude = locationProvider.getLocationLongitude().toString()
                locationSocket.sendLocation(latitude, longitude)
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }
}
