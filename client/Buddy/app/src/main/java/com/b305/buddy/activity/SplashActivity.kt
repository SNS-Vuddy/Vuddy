//package com.b305.buddy.activity
//
//import android.Manifest
//import android.content.Intent
//import android.os.Bundle
//import androidx.activity.result.ActivityResultLauncher
//import androidx.appcompat.app.AppCompatActivity
//import com.b305.buddy.databinding.ActivitySplashBinding
//import com.b305.buddy.extension.checkIsSavedUser
//import com.b305.buddy.extension.checkPermissionsResult
//import com.b305.buddy.extension.connectAndSendToLocationSocket
//import com.b305.buddy.extension.isLocationServicesAvailable
//import com.b305.buddy.extension.isRunTimePermissionsGranted
//import com.b305.buddy.extension.moveAuth
//import com.b305.buddy.extension.moveMain
//import com.b305.buddy.extension.showDialogForLocationServiceSetting
//import com.b305.buddy.util.SharedManager
//
//
///**
// * 1. 위치 권환 확인
// * 2. 소켓 서버 connect
// * 3. move main
// */
//class SplashActivity : AppCompatActivity() {
//
//    lateinit var binding: ActivitySplashBinding
//    private val sharedManager: SharedManager by lazy { SharedManager(this) }
//    var PERMISSIONS_REQUEST_CODE = 100
//    var REQUIRED_PERMISSIONS =
//        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
//    lateinit var getGPSPermissionLauncher: ActivityResultLauncher<Intent>
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySplashBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
////        checkAllPermissions()
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        checkPermissionsResult(requestCode, permissions, grantResults)
//    }
//
//    fun moveMainOrAuth() {
//        if (checkIsSavedUser(sharedManager)) {
//            connectAndSendToLocationSocket()
//            moveMain()
//            return
//        }
//        moveAuth()
//    }
//}
