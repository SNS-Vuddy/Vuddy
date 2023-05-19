package com.b305.vuddy.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.b305.vuddy.R
import com.b305.vuddy.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
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
    private lateinit var getGPSPermissionLauncher: ActivityResultLauncher<Intent>
    private lateinit var getBatteryIgnoreLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!isLocationServicesAvailable()) {
            showDialogForLocationServiceSetting()
        } else if (!isBatterIgnoreAvailable()) {
            showDialogForBatterIgnoreSetting()
        } else {
            isRunTimePermissionsGranted()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResultExtension(requestCode, grantResults)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun onRequestPermissionsResultExtension(requestCode: Int, grantResults: IntArray) {
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
                        Toast.makeText(this, R.string.splash_need_location_permission, Toast.LENGTH_LONG).show()
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
                        Toast.makeText(this, R.string.splash_need_background_location_permission, Toast.LENGTH_LONG).show()
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
                        Toast.makeText(this, R.string.splash_need_notification_permission, Toast.LENGTH_LONG).show()
                        finish()
                        return
                    }
                }
            }
        }
        isRunTimePermissionsGranted()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun isRunTimePermissionsGranted() {
        val hasFineLocationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder(this)
                .setTitle(R.string.splash_runtime_dialog_title)
                .setMessage(R.string.splash_runtime_dialog_message)
                .setPositiveButton(R.string.common_okay) { _, _ ->
                    ActivityCompat.requestPermissions(this, REQUIRED_LOCAION_PERMISSION, LOCATION_PERMISSIONS_REQUEST_CODE)
                }
                .setNegativeButton(R.string.common_cancel) { _, _ ->
                    Toast.makeText(this, R.string.splash_need_location_permission, Toast.LENGTH_LONG).show()
                    finish()
                }
                .create()
                .show()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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

    private fun isBatterIgnoreAvailable(): Boolean {
        val powerManager = getSystemService(POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(packageName)
    }

    @SuppressLint("BatteryLife")
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun showDialogForBatterIgnoreSetting() {
        val message = R.string.splash_need_battery_ignore_permission
        getBatteryIgnoreLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (isBatterIgnoreAvailable()) {
                    isRunTimePermissionsGranted()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    finish()
                }
            }

        val batteryIgnoreSettingIntent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        batteryIgnoreSettingIntent.data = Uri.parse("package:$packageName")
        getBatteryIgnoreLauncher.launch(batteryIgnoreSettingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun showDialogForLocationServiceSetting() {
        val message = R.string.splash_need_location_permission
        getGPSPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (isLocationServicesAvailable()) {
                    isRunTimePermissionsGranted()
                } else {
                    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.splash_location_dialog_title)
        builder.setMessage(R.string.splash_location_dialog_message)
        builder.setCancelable(true)
        builder.setPositiveButton(R.string.common_okay) { _, _ ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            getGPSPermissionLauncher.launch(callGPSSettingIntent)
        }
        builder.setNegativeButton(R.string.common_cancel) { dialog, _ ->
            dialog.cancel()
            Toast.makeText(this, R.string.splash_need_location_permission, Toast.LENGTH_SHORT)
                .show()
            finish()
        }
        builder.create().show()
    }

    private fun moveAuth() {
        startActivity(Intent(this, AuthActivity::class.java))
        finish()
    }
}
