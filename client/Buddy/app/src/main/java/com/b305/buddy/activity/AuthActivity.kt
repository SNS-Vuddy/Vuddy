package com.b305.buddy.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.b305.buddy.R
import com.b305.buddy.databinding.ActivityAuthBinding
import com.b305.buddy.model.AuthRequest
import com.b305.buddy.model.AuthResponse
import com.b305.buddy.model.Token
import com.b305.buddy.model.User
import com.b305.buddy.util.RetrofitAPI
import com.b305.buddy.util.SharedManager
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity() {

    lateinit var binding: ActivityAuthBinding
    private val sharedManager: SharedManager by lazy { SharedManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // true : 회원가입, false : 로그인
        var isSignup = true

        binding.btnSignup.setOnClickListener {
            isSignup = true
            binding.btnSignup.setBackgroundResource(R.color.selected)
            binding.btnLogin.setBackgroundResource(R.color.unselected)
            binding.etPasswordConfirm.visibility = android.view.View.VISIBLE
            binding.tvConfirm.visibility = android.view.View.VISIBLE
            binding.etNickname.setText("")
            binding.etPassword.setText("")
            binding.etPasswordConfirm.setText("")
            checkInput()
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
                checkInput()
            }

            binding.etPassword.doOnTextChanged() { _, _, _, _ ->
                checkInput()
            }

            binding.etPasswordConfirm.doOnTextChanged() { _, _, _, _ ->
                checkInput()
            }
        }

        binding.btnOk.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            val password = binding.etPassword.text.toString()
            val passwordConfirm = binding.etPasswordConfirm.text.toString()
            val userData = AuthRequest(nickname, password)

            if (isSignup) {
                signup(userData, passwordConfirm)
            } else {
                login(userData)
            }
        }
    }

    private fun checkInput() {
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

    private fun checkInputNicknameAndPassword(nickname: String, password: String, passwordConfirm: String): Boolean {
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

    private fun login(authRequest: AuthRequest) {
        val service = RetrofitAPI.authService

        service.login(authRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()

                    val accessToken: String = result?.accessToken.toString()
                    val refreshToken: String = result?.refreshToken.toString()
                    val token: Token = Token(accessToken, refreshToken)
                    sharedManager.saveCurrentToken(token)

                    val nickname = authRequest.nickname
                    val password = authRequest.password
                    val user = User(nickname, password)
                    sharedManager.saveCurrentUser(user)

                    val message: String = result?.message.toString()
                    Toast.makeText(this@AuthActivity, message, Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@AuthActivity, SplashActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = JSONObject(response.errorBody()?.string()!!)
                    Toast.makeText(this@AuthActivity, errorMessage.getString("message"), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@AuthActivity, "로그인 실패", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun signup(authRequest: AuthRequest, passwordConfirm: String) {
        val service = RetrofitAPI.authService
        val nickname = authRequest.nickname!!
        val password = authRequest.password!!
        val passwordConfirm = passwordConfirm

        if (!checkInputNicknameAndPassword(nickname, password, passwordConfirm)) {
            return
        }

        service.signup(authRequest).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()

                    val accessToken: String = result?.accessToken.toString()
                    val refreshToken: String = result?.refreshToken.toString()
                    val token: Token = Token(accessToken, refreshToken)
                    sharedManager.saveCurrentToken(token)

                    val user = User(nickname, password)
                    sharedManager.saveCurrentUser(user)

                    val message: String = result?.message.toString()
                    Toast.makeText(this@AuthActivity, message, Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@AuthActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = JSONObject(response.errorBody()?.string()!!)
                    Toast.makeText(this@AuthActivity, errorMessage.getString("message"), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Toast.makeText(this@AuthActivity, "회원가입 실패", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
