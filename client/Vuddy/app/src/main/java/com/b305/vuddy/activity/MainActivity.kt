package com.b305.vuddy.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.b305.vuddy.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
// Todo: listener
//        startService(Intent(this, ImmortalLocationService::class.java))
    }

//    companion object {
//        class BootReceiver : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//                if ("android.intent.action.BOOT_COMPLETED" == intent.action) {
//                    val serviceIntent = Intent(context, ImmortalLocationService::class.java)
//                    context.startService(serviceIntent)
//                }
//            }
//        }
//    }
}
