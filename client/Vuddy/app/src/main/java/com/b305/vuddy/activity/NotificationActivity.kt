package com.b305.vuddy.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.b305.vuddy.databinding.ActivityNotificationBinding

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class NotificationActivity : AppCompatActivity() {
    lateinit var binding: ActivityNotificationBinding
    private var PERMISSIONS_REQUEST_CODE = 100
    private var REQUIRED_PERMISSION = arrayOf(Manifest.permission.POST_NOTIFICATIONS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isRunTimePermissionGranted()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PERMISSIONS_REQUEST_CODE) {
            return
        }

        if (grantResults.size != REQUIRED_PERMISSION.size) {
            return
        }

        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "알림 권한을 허용으로 설정해주세요", Toast.LENGTH_LONG).show()
                finish()
                return
            }
        }

        moveAuth()
    }

    private fun isRunTimePermissionGranted() {
        val hasPostNotificationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
        if (hasPostNotificationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSION, PERMISSIONS_REQUEST_CODE)
            return
        }

        moveAuth()
    }

    private fun moveAuth() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }
}
