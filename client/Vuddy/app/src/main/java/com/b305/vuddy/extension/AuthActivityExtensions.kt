package com.b305.vuddy.extension

import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.b305.vuddy.R
import com.b305.vuddy.activity.AuthActivity
import com.b305.vuddy.activity.ConnectSocketMainActivity
import com.b305.vuddy.model.AuthRequest
import com.b305.vuddy.model.AuthResponse
import com.b305.vuddy.model.Token
import com.b305.vuddy.model.User
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun AuthActivity.checkSignupByInput() {
    val nickname = binding.etNickname.text.toString()
    val password = binding.etPassword.text.toString()
    val passwordConfirm = binding.etPasswordConfirm.text.toString()

    if (nickname.isEmpty()) {
        binding.tvConfirm.text = getString(R.string.auth_nickname_empty)
        binding.tvConfirm.setTextColor(ContextCompat.getColor(this, R.color.auth_error))
        return
    }
    if (nickname.length > 20 || nickname.length < 4) {
        binding.tvConfirm.text = getString(R.string.auth_nickname_length_error)
        binding.tvConfirm.setTextColor(ContextCompat.getColor(this, R.color.auth_error))
        return
    }
    if (password.isEmpty()) {
        binding.tvConfirm.text = getString(R.string.auth_password_empty)
        binding.tvConfirm.setTextColor(ContextCompat.getColor(this, R.color.auth_error))
        return
    }
    if (password.length > 20 || password.length < 8) {
        binding.tvConfirm.text = getString(R.string.auth_password_length_error)
        binding.tvConfirm.setTextColor(ContextCompat.getColor(this, R.color.auth_error))
        return
    }
    if (password != passwordConfirm) {
        binding.tvConfirm.text = getString(R.string.auth_password_confirm_error)
        binding.tvConfirm.setTextColor(ContextCompat.getColor(this, R.color.auth_error))
        return
    }
    binding.tvConfirm.text = getString(R.string.auth_success)
    binding.tvConfirm.setTextColor(ContextCompat.getColor(this, R.color.auth_success))
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

fun AuthActivity.signupService(authRequest: AuthRequest) {
    service.signup(authRequest).enqueue(object : Callback<AuthResponse> {
        override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
            if (response.isSuccessful) {

                val nickname = authRequest.nickname
                val password = authRequest.password
                val user = User(nickname, password)
                sharedManager.saveCurrentUser(user)

                val result = response.body()
                val accessToken: String = result?.accessToken.toString()
                val refreshToken: String = result?.refreshToken.toString()
                val token: Token = Token(accessToken, refreshToken)
                sharedManager.saveCurrentToken(token)


                val message: String = result?.message.toString()
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                val intent = Intent(applicationContext, ConnectSocketMainActivity::class.java)
                startActivity(intent)
                finish()
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
                val user = User(nickname, password)
                sharedManager.saveCurrentUser(user)

                val accessToken: String = result?.accessToken.toString()
                val refreshToken: String = result?.refreshToken.toString()
                val token: Token = Token(accessToken, refreshToken)
                sharedManager.saveCurrentToken(token)


                val message: String = result?.message.toString()
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()

                val intent = Intent(applicationContext, ConnectSocketMainActivity::class.java)
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
