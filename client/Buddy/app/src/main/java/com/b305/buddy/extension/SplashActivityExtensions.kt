//package com.b305.buddy.extension
//
//import android.Manifest
//import android.content.Context
//import android.content.DialogInterface
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.location.LocationManager
//import android.os.Handler
//import android.provider.Settings
//import android.widget.Toast
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AlertDialog
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.b305.buddy.activity.AuthActivity
//import com.b305.buddy.activity.MainActivity
//import com.b305.buddy.activity.SplashActivity
//import com.b305.buddy.model.Token
//import com.b305.buddy.model.User
//import com.b305.buddy.util.LocationProvider
//import com.b305.buddy.util.LocationSocket
//import com.b305.buddy.util.SharedManager
//import kotlinx.coroutines.Runnable
//
//fun checkIsSavedUser(sharedManager: SharedManager): Boolean {
//    val token: Token = sharedManager.getCurrentToken()
//    val accessToken = token.accessToken
//    val refreshToken = token.refreshToken
//    val user: User = sharedManager.getCurrentUser()
//    val nickname = user.nickname
//    val password = user.password
//
//
//    return !(accessToken == "" || refreshToken == "" || nickname == "" || password == "")
//}
//
//fun SplashActivity.moveMain() {
//    Handler().postDelayed({
//        startActivity(Intent(this, MainActivity::class.java))
//        finish()
//    }, 1000)
//}
//
//fun SplashActivity.moveAuth() {
//    startActivity(Intent(this, AuthActivity::class.java))
//}
//
//fun SplashActivity.connectAndSendToLocationSocket() {
//    val locationSocket = LocationSocket(this)
//    val locationProvider = LocationProvider(this)
//
//    locationSocket.connection()
//    val handler = Handler()
//    val runnable = object : Runnable {
//        override fun run() {
//            val latitude = locationProvider.getLocationLatitude().toString()
//            val longitude = locationProvider.getLocationLongitude().toString()
//            locationSocket.sendLocation(latitude, longitude)
//            handler.postDelayed(this, 1000)
//        }
//    }
//    handler.post(runnable)
//}
//
//fun SplashActivity.isLocationServicesAvailable(): Boolean {
//    val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
//
//    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//        return true
//    }
//
//    if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//        return true
//    }
//
//    return false
//}
//
//fun SplashActivity.isRunTimePermissionsGranted() {
//    val hasFineLocationPermission =
//        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//    val hasCoarseLocationPermission =
//        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//    if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED || hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
//        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
//        return
//    }
//
//    moveMainOrAuth()
//}
//
//fun SplashActivity.checkPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//    if (requestCode != PERMISSIONS_REQUEST_CODE) {
//        return
//    }
//
//    if (grantResults.size != REQUIRED_PERMISSIONS.size) {
//        return
//    }
//
//    for (result in grantResults) {
//        if (result != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(this, "권한이 필요합니다.", Toast.LENGTH_SHORT).show()
//            finish()
//            return
//        }
//    }
//
//    moveMainOrAuth()
//}
//
//fun SplashActivity.showDialogForLocationServiceSetting() {
//    getGPSPermissionLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (isLocationServicesAvailable()) {
//                isRunTimePermissionsGranted()
//            } else {
//                Toast.makeText(this, "위치 서비스를 사용할 수 없습니다.", Toast.LENGTH_LONG).show()
//                finish()
//            }
//        }
//
//    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//    builder.setTitle("위치 서비스 비활성화")
//    builder.setMessage("위치 서비스가 꺼져있습니다. 설정해야 앱을 사용할 수 있습니다.")
//    builder.setCancelable(true)
//    builder.setPositiveButton("설정", DialogInterface.OnClickListener { dialog, id ->
//        val callGPSSettingIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
//        getGPSPermissionLauncher.launch(callGPSSettingIntent)
//    })
//    builder.setNegativeButton("취소", DialogInterface.OnClickListener { dialog, id ->
//        dialog.cancel()
//        Toast.makeText(this, "기기에서 위치서비스(GPS) 설정 후 사용해주세요.", Toast.LENGTH_SHORT).show()
//        finish()
//    })
//    builder.create().show()
//}
