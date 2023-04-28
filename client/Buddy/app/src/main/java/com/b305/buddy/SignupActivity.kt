package com.b305.buddy

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.b305.buddy.data.SharedManager
import com.b305.buddy.data.Token
import com.b305.buddy.retrofit.RetrofitAPI
import com.b305.buddy.retrofit.SignupRequest
import com.b305.buddy.retrofit.SignupResponse
import retrofit2.Call
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    
    private val sharedManager: SharedManager by lazy { SharedManager(this) }
    
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        
        
        val okBtn = findViewById<Button>(R.id.btn_signup_ok)
        okBtn.setOnClickListener {
            val nickName = findViewById<EditText>(R.id.et_signup_id)?.text.toString()
            val password = findViewById<EditText>(R.id.et_signup_pw)?.text.toString()
            val profileImage = "tempProfileImage"
            val statusMessage = "tempStatusMessage"
            val userData = SignupRequest(nickName, password, profileImage, statusMessage)
            signup(userData)
        }
        
        val cancelBtn = findViewById<Button>(R.id.btn_signup_cancel)
        cancelBtn.setOnClickListener {
            finish()
        }
        
        val refreshBtn = findViewById<Button>(R.id.btn_refresh)
        refreshBtn.setOnClickListener {
            val token = sharedManager.getCurrentToken()
            
            findViewById<TextView>(R.id.tv_access_token).text = token.accessToken
            findViewById<TextView>(R.id.tv_refresh_token).text = token.refreshToken
        }
    }
    
    private fun signup(signupRequest: SignupRequest) {
        val service = RetrofitAPI.signupService
        
        service.signup(signupRequest).enqueue(object : retrofit2.Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    
                    val accessToken: String = result?.accessToken.toString()
                    
                    //Todo : refresh token 받아오기
                    val refreshToken: String = "test refreshToken"
                    
                    val token: Token = Token(accessToken, refreshToken)
                    
                    sharedManager.saveCurrentToken(token)
                    Log.d("회원가입 성공", "$result")
                    finish()
                }
            }
            
            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.d("회원가입 실패", t.message.toString())
            }
        })
    }
}