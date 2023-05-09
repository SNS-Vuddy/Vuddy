package com.b305.vuddy.activity

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.b305.vuddy.databinding.ActivitySplashBinding

/**
 * location available
 * permission: location -> back -> noti -> auth -> main
 * splash = location, backlocation, noti
 * Todo 스플래시에 합치기
 * Todo 익스텐션으로 이동
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    private var LOCATION_PERMISSIONS_REQUEST_CODE = 101
    private var BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE = 102
    private var NOTIFICATION_PERMISSIONS_REQUEST_CODE = 103
    private var REQUIRED_LOCAION_PERMISSION =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    private var REQUIRED_BACKGROUND_LOCATION_PERMISSION = arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    private var REQUIRED_NOTIFICATION_PERMISSION = arrayOf(Manifest.permission.POST_NOTIFICATIONS)
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

        if (requestCode != LOCATION_PERMISSIONS_REQUEST_CODE && requestCode != BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE && requestCode != NOTIFICATION_PERMISSIONS_REQUEST_CODE) {
            return
        }

        when (requestCode) {
            LOCATION_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.size != REQUIRED_LOCAION_PERMISSION.size) {
                    return
                }

                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "위치 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                        finish()
                        return
                    }
                }
            }

            BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.size != REQUIRED_BACKGROUND_LOCATION_PERMISSION.size) {
                    return
                }

                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "백그라운드 위치 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                        finish()
                        return
                    }
                }
            }

            NOTIFICATION_PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.size != REQUIRED_NOTIFICATION_PERMISSION.size) {
                    return
                }

                for (result in grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "알림 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                        finish()
                        return
                    }
                }
            }
        }

        isRunTimePermissionsGranted()
    }

    private fun isRunTimePermissionsGranted() {
        val hasFineLocationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUIRED_LOCAION_PERMISSION, LOCATION_PERMISSIONS_REQUEST_CODE)
            return
        }

        val hasBackgroundLocationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        if (hasBackgroundLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_BACKGROUND_LOCATION_PERMISSION,
                BACKGROUND_LOCATION_PERMISSIONS_REQUEST_CODE
            )
            return
        }

        val hasNotificationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
        if (hasNotificationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_NOTIFICATION_PERMISSION,
                NOTIFICATION_PERMISSIONS_REQUEST_CODE
            )
            return
        }

        moveAuth()
    }

    private fun moveAuth() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }

    private fun isLocationServicesAvailable(): Boolean {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return true
        }

        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            return true
        }

        return false
    }

    private fun showDialogForLocationServiceSetting() {
        getGPSPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (isLocationServicesAvailable()) {
                    isRunTimePermissionsGranted()
                } else {
                    Toast.makeText(this, "위치 서비스를 사용할 수 없습니다.", Toast.LENGTH_LONG).show()
                    finish()
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("위치 서비스가 꺼져있습니다. 설정해야 앱을 사용할 수 있습니다.")
        builder.setCancelable(true)
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            getGPSPermissionLauncher.launch(callGPSSettingIntent)
        })
        builder.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
            Toast.makeText(this, "기기에서 위치서비스(GPS) 설정 후 사용해주세요.", Toast.LENGTH_SHORT).show()
            finish()
        })
        builder.create().show()
    }
}
