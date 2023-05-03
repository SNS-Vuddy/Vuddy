package com.b305.buddy.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.b305.buddy.databinding.ActivityAdminBinding
import com.b305.buddy.util.SharedManager

// admin
// adminadmin
class AdminActivity : AppCompatActivity() {
    
    lateinit var binding: ActivityAdminBinding
    private val sharedManager: SharedManager by lazy { SharedManager(this) }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnLogout.setOnClickListener {
            sharedManager.removeCurrentToken()
            sharedManager.removeCurrentToken()
            Toast.makeText(this, "로그아웃 성공", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}