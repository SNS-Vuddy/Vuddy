package com.b305.vuddy.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.b305.vuddy.databinding.ActivityBackgroundLocationPermissionBinding

class BackgroundLocationPermissionActivity : AppCompatActivity() {
    lateinit var binding: ActivityBackgroundLocationPermissionBinding
    private var PERMISSIONS_REQUEST_CODE = 100
    private var REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBackgroundLocationPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkBackgroundLocationPermission()
//        showBackgroundLocationPermissionDialog()
    }

    private fun showBackgroundLocationPermissionDialog() {
        val builder = AlertDialog.Builder(this)
            .setTitle("백그라운드 위치 권한을 항상 허용으로 설정해주세요")
        val listener = DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                DialogInterface.BUTTON_POSITIVE ->
                    ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
            }
        }
        builder
            .setPositiveButton("설정", listener)
            .setNegativeButton("취소", null)
            .show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PERMISSIONS_REQUEST_CODE) {
            return
        }

        if (grantResults.size != REQUIRED_PERMISSIONS.size) {
            return
        }

        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "백그라운드 위치 권한을 항상 허용으로 설정해주세요", Toast.LENGTH_SHORT).show()
                finish()
                return
            }
        }

        moveAuth()
    }

    private fun checkBackgroundLocationPermission() {
        val hasBackgroundLocationPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        if (hasBackgroundLocationPermission != PackageManager.PERMISSION_GRANTED) {
            showBackgroundLocationPermissionDialog()
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
