package com.b305.buddy.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.b305.buddy.databinding.ActivityAdminBinding
import com.b305.buddy.util.LocationProvider
import com.b305.buddy.util.SharedManager
import com.b305.buddy.util.ChatSocket
import kotlinx.coroutines.Runnable

// admin
// adminadmin
class AdminActivity : AppCompatActivity() {

    lateinit var binding: ActivityAdminBinding
    private val sharedManager: SharedManager by lazy { SharedManager(this) }
    private val socket = ChatSocket(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvCurrentNickname.text = sharedManager.getCurrentUser().nickname
        setButton()
    }

    private fun sendLocation() {
        val locationProvider = LocationProvider(this)
        val latitude = locationProvider.getLocationLatitude().toString()
        val longitude = locationProvider.getLocationLongitude().toString()

        socket.sendLocation(latitude, longitude)
    }

    private fun setButton() {
        // 로그아웃 버튼
        binding.btnLogout.setOnClickListener {
            sharedManager.removeCurrentToken()
            sharedManager.removeCurrentUser()
            Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 소켓 연결 버튼
        binding.btnConnect.setOnClickListener {
            Log.d("AdminActivity", "connection")
            socket.connection()
        }

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                Log.d("AdminActivity", "sendLocation")
                sendLocation()
                handler.postDelayed(this, 3000)
            }
        }

        // 소켓 인터벌 전송 버튼
        binding.btnSend.setOnClickListener {
            handler.post(runnable)
        }

        // 소켓 인터벌 전송 정지 버튼
        binding.btnStop.setOnClickListener {
            handler.removeCallbacks(runnable)
        }

        // 이모탈 시작 버튼
        binding.btnStartImmortal.setOnClickListener {
            Log.d("AdminActivity", "Click Immortal Button")
        }

        // 서비스 A 정지 버튼
        binding.btnEndA.setOnClickListener {
            Log.d("AdminActivity", "Click EndA Button")
        }

        // 서비스 B 정지 버튼
        binding.btnEndB.setOnClickListener {
            Log.d("AdminActivity", "Click EndB Button")
        }

        // 서비스 둘다 정지 버튼
        binding.btnEndBoth.setOnClickListener {
            Log.d("AdminActivity", "Click EndBoth Button")
        }
    }
}
