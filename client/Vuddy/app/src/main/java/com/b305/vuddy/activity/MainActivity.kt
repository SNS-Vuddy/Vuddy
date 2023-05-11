package com.b305.vuddy.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.b305.vuddy.R
import com.b305.vuddy.service.TestService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startService(Intent(this, TestService::class.java))
    }

    companion object {
        class BootReceiver : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
                    val serviceIntent = Intent(context, TestService::class.java)
                    context.startService(serviceIntent)
                }
            }
        }
    }
}
