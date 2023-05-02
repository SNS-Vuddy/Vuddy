package com.b305.buddy

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        }
        
        binding.btnLogin.setOnClickListener {
            isSignup = false
            binding.btnSignup.setBackgroundResource(R.color.unselected)
            binding.btnLogin.setBackgroundResource(R.color.selected)
        }
        
        binding.btnOk.setOnClickListener {
            val nickname = binding.etNickname.text.toString()
            val password = binding.etPassword.text.toString()
            val userData = AuthRequest(nickname, password)
            
            if (isSignup) {
                signup(userData)
            } else {
                login(userData)
            }
        }
        
        binding.btnCancel.setOnClickListener {
            finish()
        }
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
                    
                    val intent = Intent(this@AuthActivity, MainActivity::class.java)
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
    
    private fun signup(authRequest: AuthRequest) {
        val service = RetrofitAPI.authService
        
        service.signup(authRequest).enqueue(object : Callback<AuthResponse> {
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