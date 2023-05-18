package com.b305.vuddy.view.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.widget.doOnTextChanged
import com.b305.vuddy.R
import com.b305.vuddy.databinding.ActivityAuthBinding
import com.b305.vuddy.view.extension.checkSignupByInput
import com.b305.vuddy.view.extension.confirmSignupByInput
import com.b305.vuddy.view.extension.loginService
import com.b305.vuddy.view.extension.signupService
import com.b305.vuddy.model.AuthRequest
import com.b305.vuddy.model.Token
import com.b305.vuddy.model.User
import com.b305.vuddy.service.RetrofitAPI
import com.b305.vuddy.util.SharedManager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AuthActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthBinding
    val sharedManager: SharedManager by lazy { SharedManager(this) }
    val service = RetrofitAPI.authService
    private var isSignup = true
    private val photoList = ArrayList<Uri>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (checkIsSavedUser(sharedManager)) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSignup.setOnClickListener {
            isSignup = true
            binding.btnSignup.setBackgroundResource(R.color.selected)
            binding.btnLogin.setBackgroundResource(R.color.unselected)
            binding.etPasswordConfirm.visibility = android.view.View.VISIBLE
            binding.tvConfirm.visibility = android.view.View.VISIBLE
            binding.etNickname.setText("")
            binding.etPassword.setText("")
            binding.etPasswordConfirm.setText("")
            checkSignupByInput()
        }

        binding.btnLogin.setOnClickListener {
            isSignup = false
            binding.btnSignup.setBackgroundResource(R.color.unselected)
            binding.btnLogin.setBackgroundResource(R.color.selected)
            binding.etPasswordConfirm.visibility = android.view.View.INVISIBLE
            binding.tvConfirm.visibility = android.view.View.INVISIBLE
            binding.etNickname.setText("")
            binding.etPassword.setText("")
        }

        if (isSignup) {
            binding.etNickname.doOnTextChanged { _, _, _, _ ->
                checkSignupByInput()
            }

            binding.etPassword.doOnTextChanged { _, _, _, _ ->
                checkSignupByInput()
            }

            binding.etPasswordConfirm.doOnTextChanged { _, _, _, _ ->
                checkSignupByInput()
            }
        }

        binding.btnOk.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            val password = binding.etPassword.text.toString()
            val passwordConfirm = binding.etPasswordConfirm.text.toString()
            val authRequest = AuthRequest(nickname, password)

            if (isSignup && confirmSignupByInput(nickname, password, passwordConfirm)) {
                signupService(authRequest)
            } else {
                loginService(authRequest)
            }
        }
    }

    private fun checkIsSavedUser(sharedManager: SharedManager): Boolean {
        val token: Token = sharedManager.getCurrentToken()
        val accessToken = token.accessToken
        val refreshToken = token.refreshToken
        val user: User = sharedManager.getCurrentUser()
        val nickname = user.nickname
        val password = user.password


        return !(accessToken == "" || refreshToken == "" || nickname == "" || password == "")
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val REQUEST_IMAGE_PICK = 1
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                photoList.add(imageUri)
            }
        }
    }
    fun openDialog() {
        val dialogLayout = LayoutInflater.from(this).inflate(R.layout.dialog_img_change, null)
        val dialogBuild = AlertDialog.Builder(this).apply {
            setView(dialogLayout)
        }
        val dialog = dialogBuild.create().apply { show() }

        val imgChangeBtn = dialogLayout.findViewById<AppCompatImageView>(R.id.profile_change_camerabtn)

        val REQUEST_IMAGE_PICK = 1
        imgChangeBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)

            intent.type = "image/*"
//            intent.data = MediaStore.Images.Media.CONTENT_TYPE
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
//            intent.action = Intent.ACTION_PICK
            intent.action = Intent.ACTION_GET_CONTENT;
            startActivityForResult(intent, REQUEST_IMAGE_PICK)

        }
        val imgChangeCompleteBtn = dialogLayout.findViewById<Button>(R.id.profile_changeimg_complete_btn)
        imgChangeCompleteBtn.setOnClickListener {
            imgChange()
            dialog.dismiss()
        }
    }

    fun imgChange() {
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        // Uri를 File로 변환하는 메소드

        val imageUri = photoList[0]
        val file = File(getPathFromUri(imageUri))
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())

        builder.addFormDataPart("images", file.name, requestFile)

        val body = builder.build().parts

        val call = RetrofitAPI.userService.profileImgChange(body)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val message = response.body()?.string()
                    Log.d("프로필 교체성공", "All uploaded successfully. Response: $message")

                } else {
                    Log.d("프로필 교체 실패", "upload failed. Response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("프로필 교체 실패", "upload failed.", t)
            }
        })
    }

    fun AuthActivity.getPathFromUri(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = this.contentResolver.query(uri, projection, null, null, null)!!
        val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(columnIndex)
    }
}
