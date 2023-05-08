package com.b305.vuddy.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.b305.vuddy.R
import com.b305.vuddy.util.LocationProvider
import com.b305.vuddy.util.LocationSocket

class ConnectSocketMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_socket_main)
        connectAndSendToLocationSocket()
        moveMain()
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

    private fun moveMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
