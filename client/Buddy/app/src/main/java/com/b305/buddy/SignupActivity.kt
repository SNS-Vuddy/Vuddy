package com.b305.buddy

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.b305.buddy.retrofit.RetrofitWork
import com.b305.buddy.retrofit.SignupRequest

class SignupActivity : AppCompatActivity() {
    
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
            
            val retrofitWork = RetrofitWork(userData)
            retrofitWork.work()
        }
        
        val cancelBtn = findViewById<Button>(R.id.btn_signup_cancel)
        cancelBtn.setOnClickListener {
            finish()
        }
    }
}