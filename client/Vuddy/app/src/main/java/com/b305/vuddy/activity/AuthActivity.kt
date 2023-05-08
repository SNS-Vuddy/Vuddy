package com.b305.vuddy.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.b305.vuddy.R
import com.b305.vuddy.databinding.ActivityAuthBinding
import com.b305.vuddy.extension.checkSignupByInput
import com.b305.vuddy.extension.confirmSignupByInput
import com.b305.vuddy.extension.loginService
import com.b305.vuddy.extension.signupService
import com.b305.vuddy.model.AuthRequest
import com.b305.vuddy.model.Token
import com.b305.vuddy.model.User
import com.b305.vuddy.util.RetrofitAPI
import com.b305.vuddy.util.SharedManager

class AuthActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthBinding
    val sharedManager: SharedManager by lazy { SharedManager(this) }
    val service = RetrofitAPI.authService
    var isSignup = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (checkIsSavedUser(sharedManager)) {
            val intent = Intent(applicationContext, ConnectSocketMainActivity::class.java)
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
            binding.etNickname.doOnTextChanged() { _, _, _, _ ->
                checkSignupByInput()
            }

            binding.etPassword.doOnTextChanged() { _, _, _, _ ->
                checkSignupByInput()
            }

            binding.etPasswordConfirm.doOnTextChanged() { _, _, _, _ ->
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

    fun checkIsSavedUser(sharedManager: SharedManager): Boolean {
        val token: Token = sharedManager.getCurrentToken()
        val accessToken = token.accessToken
        val refreshToken = token.refreshToken
        val user: User = sharedManager.getCurrentUser()
        val nickname = user.nickname
        val password = user.password


        return !(accessToken == "" || refreshToken == "" || nickname == "" || password == "")
    }
}
