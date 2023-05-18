package com.b305.vuddy.view.extension

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.b305.vuddy.R
import com.b305.vuddy.view.activity.AuthActivity
import com.b305.vuddy.view.activity.MainActivity
import com.b305.vuddy.model.AuthRequest
import com.b305.vuddy.model.AuthResponse
import com.b305.vuddy.model.Token
import com.b305.vuddy.model.User
import com.b305.vuddy.service.RetrofitAPI
import com.b305.vuddy.util.BASE_PROFILE_IMG_URL
import com.b305.vuddy.util.BASIC_IMG_URL
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


private val photoList = ArrayList<Uri>()
fun AuthActivity.checkSignupByInput() {
    val nickname = binding.etNickname.text.toString()
    val password = binding.etPassword.text.toString()
    val passwordConfirm = binding.etPasswordConfirm.text.toString()

    if (nickname.isEmpty()) {
        binding.tvConfirm.text = getString(R.string.auth_nickname_empty)
        binding.tvConfirm.setTextColor(ContextCompat.getColor(this, R.color.error))
        return
    }
    if (nickname.length > 20 || nickname.length < 4) {
        binding.tvConfirm.text = getString(R.string.auth_nickname_length_error)
        binding.tvConfirm.setTextColor(ContextCompat.getColor(this, R.color.error))
        return
    }
    if (password.isEmpty()) {
        binding.tvConfirm.text = getString(R.string.auth_password_empty)
        binding.tvConfirm.setTextColor(ContextCompat.getColor(this, R.color.error))
        return
    }
    if (password.length > 20 || password.length < 8) {
        binding.tvConfirm.text = getString(R.string.auth_password_length_error)
        binding.tvConfirm.setTextColor(ContextCompat.getColor(this, R.color.error))
        return
    }
    if (password != passwordConfirm) {
        binding.tvConfirm.text = getString(R.string.auth_password_confirm_error)
        binding.tvConfirm.setTextColor(ContextCompat.getColor(this, R.color.error))
        return
    }
    binding.tvConfirm.text = getString(R.string.auth_success)
    binding.tvConfirm.setTextColor(ContextCompat.getColor(this, R.color.selected))
}

fun AuthActivity.confirmSignupByInput(nickname: String, password: String, passwordConfirm: String): Boolean {
    if (nickname.isEmpty()) {
        Toast.makeText(this, R.string.auth_nickname_empty, Toast.LENGTH_SHORT).show()
        return false
    }

    if (nickname.length > 20 || nickname.length < 4) {
        Toast.makeText(this, R.string.auth_nickname_length_error, Toast.LENGTH_SHORT).show()
        return false
    }

    if (password.isEmpty()) {
        Toast.makeText(this, R.string.auth_password_empty, Toast.LENGTH_SHORT).show()
        return false
    }

    if (password.length > 20 || password.length < 8) {
        Toast.makeText(this, R.string.auth_password_empty, Toast.LENGTH_SHORT).show()
        return false
    }

    if (password != passwordConfirm) {
        Toast.makeText(this, R.string.auth_password_confirm_error, Toast.LENGTH_SHORT).show()
        return false
    }

    return true
}

fun AuthActivity.changeProfileImgDialog() {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(R.string.auth_profile_img_dialog_title)
    builder.setMessage(R.string.auth_profile_img_dialog_message)
    builder.setPositiveButton(R.string.common_okay) { _, _ ->
        //TODO: 여기서 프로필 사진 변경
        openDialog()
    }
    builder.setNegativeButton(R.string.common_cancel) { _, _ ->
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
    builder.show()
}

fun AuthActivity.signupService(authRequest: AuthRequest) {
    service.signup(authRequest).enqueue(object : Callback<AuthResponse> {
        override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
            if (response.isSuccessful) {

                val nickname = authRequest.nickname
                val password = authRequest.password
                val profileImgUrl = BASE_PROFILE_IMG_URL
                val statusImgUrl = BASIC_IMG_URL
                val user = User(nickname, password, profileImgUrl, statusImgUrl)
                sharedManager.saveCurrentUser(user)

                val result = response.body()
                val accessToken: String = result?.accessToken.toString()
                val refreshToken: String = result?.refreshToken.toString()
                val token = Token(accessToken, refreshToken)
                sharedManager.saveCurrentToken(token)

                val message: String = result?.message.toString()
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

//                changeProfileImgDialog()
            } else {
                val errorMessage = JSONObject(response.errorBody()?.string()!!)
                Toast.makeText(applicationContext, errorMessage.getString("message"), Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
            Toast.makeText(applicationContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
        }
    })
}

fun AuthActivity.loginService(authRequest: AuthRequest) {
    service.login(authRequest).enqueue(object : Callback<AuthResponse> {
        override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
            if (response.isSuccessful) {
                val result = response.body()

                val nickname = authRequest.nickname
                val password = authRequest.password

                val accessToken: String = result?.accessToken.toString()
                val refreshToken: String = result?.refreshToken.toString()
                val profileImgUrl = result?.profileImage.toString()
                val statusImgUrl = BASIC_IMG_URL
                val user = User(nickname, password, profileImgUrl, statusImgUrl)
                val token = Token(accessToken, refreshToken)
                sharedManager.saveCurrentUser(user)
                sharedManager.saveCurrentToken(token)

                val message: String = result?.message.toString()
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val errorMessage = JSONObject(response.errorBody()?.string()!!)
                Toast.makeText(applicationContext, errorMessage.getString("message"), Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
            Toast.makeText(applicationContext, "로그인 실패", Toast.LENGTH_SHORT).show()
        }
    })
}

fun AuthActivity.openDialog() {
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

fun AuthActivity.imgChange() {
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
    //...

    fun AuthActivity.onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val REQUEST_IMAGE_PICK = 1
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = data?.data
            if (imageUri != null) {
                photoList.add(imageUri)
            }
        }
    }

