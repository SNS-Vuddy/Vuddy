package com.b305.vuddy.view.activity

import android.app.ActivityManager
import android.content.*
import android.os.Bundle
import android.os.IBinder
import androidx.appcompat.app.AppCompatActivity
import com.b305.vuddy.R
import com.b305.vuddy.service.ImmortalService
import com.b305.vuddy.view.fragment.MessageFragment

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private var service: ImmortalService? = null
    private var serviceConnection: ServiceConnection? = null

    private val notificationReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val nickname = intent?.getStringExtra("nickname")

            // MessageFragment로 이동하는 코드 작성
            if (nickname != null) {
                val bundle = Bundle().apply {
                    putString("nickname", nickname)
                }
                val messageFragment = MessageFragment()
                messageFragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView, messageFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 등록된 BroadcastReceiver를 생성한 IntentFilter로 등록합니다.
        val filter = IntentFilter("com.b305.vuddy.MESSAGE_FRAGMENT_ACTION")
        registerReceiver(notificationReceiver, filter)

        val isImmortalServiceRunning = isServiceRunning(this, ImmortalService::class.java)
        if (!isImmortalServiceRunning) {
            val serviceIntent = Intent(this, ImmortalService::class.java)
            startService(serviceIntent)
            serviceConnection = object : ServiceConnection {
                override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
                    val serviceBinder = binder as ImmortalService.LocalBinder?
                    service = serviceBinder?.getService()
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    service = null
                }
            }
            bindService(serviceIntent, serviceConnection!!, Context.BIND_AUTO_CREATE)
        }
        //TODO: 위치 공유하지 않고 받기만 하는 모드
//        startService(Intent(this, ImmortalChatService::class.java))
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
                    val serviceIntent = Intent(context, ImmortalService::class.java)
                    context.startService(serviceIntent)
                }
            }
        }
    }

    fun getServiceInstance(): ImmortalService? {
        return service
    }

    override fun onDestroy() {
        super.onDestroy()
        if (serviceConnection != null) {
            unbindService(serviceConnection!!)
        }
    }
}
