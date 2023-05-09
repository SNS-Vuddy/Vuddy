package com.b305.vuddy.activity

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.b305.vuddy.databinding.ActivitySplashBinding
import com.b305.vuddy.extension.isLocationServicesAvailable
import com.b305.vuddy.extension.isRunTimePermissionsGranted
import com.b305.vuddy.extension.onRequestPermissionsResultExtension
import com.b305.vuddy.extension.showDialogForLocationServiceSetting

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    var LOCATION_PERMISSIONS_REQUEST_CODE = 101
    var BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE = 102
    var NOTIFICATION_PERMISSIONS_REQUEST_CODE = 103
    var REQUIRED_LOCAION_PERMISSION =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    var REQUIRED_BACKGROUND_LOCATION_PERMISSION = arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    var REQUIRED_NOTIFICATION_PERMISSION = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    lateinit var getGPSPermissionLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isLocationServicesAvailable()) {
            showDialogForLocationServiceSetting()
        } else {
            isRunTimePermissionsGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResultExtension(requestCode, permissions, grantResults)
    }
}
