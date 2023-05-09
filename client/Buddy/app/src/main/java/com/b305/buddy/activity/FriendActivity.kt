package com.b305.buddy.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.b305.buddy.R

class FriendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend)

        val addFriendBtn = findViewById<Button>(R.id.btn_add_friend)
        val requestFriendBtn = findViewById<Button>(R.id.btn_request_friend)


    }
}
