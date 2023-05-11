package com.b305.vuddy.activity

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.b305.vuddy.R
import com.b305.vuddy.service.ImmortalLocationService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val isImmortalLocationServiceRunning = isServiceRunning(this, ImmortalLocationService::class.java)
        if (!isImmortalLocationServiceRunning) {
            startService(Intent(this, ImmortalLocationService::class.java))
        }
    }

    private fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    companion object {
        class BootReceiver : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
                    val serviceIntent = Intent(context, ImmortalLocationService::class.java)
                    context.startService(serviceIntent)
                }
            }
        }
    }
}
