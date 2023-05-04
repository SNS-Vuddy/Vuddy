package com.b305.buddy.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.b305.buddy.R
import com.b305.buddy.databinding.ActivityAuthBinding
import com.b305.buddy.extension.checkSignupByInput
import com.b305.buddy.extension.confirmSignupByInput
import com.b305.buddy.extension.loginService
import com.b305.buddy.extension.signupService
import com.b305.buddy.model.AuthRequest
import com.b305.buddy.util.RetrofitAPI
import com.b305.buddy.util.SharedManager

class AuthActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthBinding
    val sharedManager: SharedManager by lazy { SharedManager(this) }
    val service = RetrofitAPI.authService
    var isSignup = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)


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
}
