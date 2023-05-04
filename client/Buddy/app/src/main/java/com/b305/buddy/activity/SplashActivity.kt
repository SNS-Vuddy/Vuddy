package com.b305.buddy.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.b305.buddy.databinding.ActivitySplashBinding
import com.b305.buddy.model.Token
import com.b305.buddy.model.User
import com.b305.buddy.util.LocationProvider
import com.b305.buddy.util.LocationSocket
import com.b305.buddy.util.SharedManager
import kotlinx.coroutines.Runnable

/**
 * 1. 위치 권환 확인
 * 2. 소켓 서버 connect
 * 3. move main
 */
class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    private val sharedManager: SharedManager by lazy { SharedManager(this) }
    private val locationSocket = LocationSocket(this)
    private val PERMISSIONS_REQUEST_CODE = 100
    var REQUIRED_PERMISSIONS =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
    lateinit var getGPSPermissionLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAllPermissions()
    }

    // 2.
    private fun connectToLocationSocket() {
        Log.d("SplashActivity", "connect to location socket")
        locationSocket.connection()

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                Log.d("SplashActivity", "send location")
                sendLocationToSocket()
                handler.postDelayed(this, 3000)
            }
        }

        handler.post(runnable)
    }


    private fun sendLocationToSocket() {
        val locationProvider = LocationProvider(this)
        val latitude = locationProvider.getLocationLatitude().toString()
        val longitude = locationProvider.getLocationLongitude().toString()

        locationSocket.sendLocation(latitude, longitude)
    }

    // 3.
    private fun moveMain() {
        val token: Token = sharedManager.getCurrentToken()
        val accessToken = token.accessToken
        val refreshToken = token.refreshToken
        val user: User = sharedManager.getCurrentUser()
        val nickname = user.nickname
        val password = user.password

        connectToLocationSocket()
        Handler().postDelayed({
            if (accessToken == "" || refreshToken == "" || nickname == "" || password == "") {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }, 2000)
    }

    private fun checkAllPermissions() {
        if (!isLocationServicesAvailable()) {
            showDialogForLocationServiceSetting();
        } else {
            isRunTimePermissionsGranted();
        }
    }

    private fun isLocationServicesAvailable(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        ))
    }

    private fun isRunTimePermissionsGranted() {
        val hasFineLocationPermission =
            ContextCompat.checkSelfPermission(this@SplashActivity, Manifest.permission.ACCESS_FINE_LOCATION)
        val hasCoarseLocationPermission =
            ContextCompat.checkSelfPermission(this@SplashActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this@SplashActivity, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        } else {
            moveMain()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size) {

            var checkResult = true

            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false
                    break
                }
            }

            if (!checkResult) {
                Toast.makeText(this@SplashActivity, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show()
                finish()
            } else {
                moveMain()
            }
        }
    }

    private fun showDialogForLocationServiceSetting() {
        getGPSPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (isLocationServicesAvailable()) {
                    isRunTimePermissionsGranted()
                } else {
                    Toast.makeText(this@SplashActivity, "위치 서비스를 사용할 수 없습니다.", Toast.LENGTH_LONG).show()
                    finish()
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this@SplashActivity)
        builder.setTitle("위치 서비스 비활성화")
        builder.setMessage("위치 서비스가 꺼져있습니다. 설정해야 앱을 사용할 수 있습니다.")
        builder.setCancelable(true)
        builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
            val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            getGPSPermissionLauncher.launch(callGPSSettingIntent)
        })
        builder.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, id ->
            dialog.cancel()
            Toast.makeText(this@SplashActivity, "기기에서 위치서비스(GPS) 설정 후 사용해주세요.", Toast.LENGTH_SHORT).show()
            finish()
        })
        builder.create().show()
    }
}
