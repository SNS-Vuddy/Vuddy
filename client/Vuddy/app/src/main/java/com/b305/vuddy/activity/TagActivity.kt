package com.b305.vuddy.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.b305.vuddy.R

class TagActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tag)

        val addFriendBtn = findViewById<Button>(R.id.btn_add_friend)
        val requestFriendBtn = findViewById<Button>(R.id.btn_request_friend)
    }
}
