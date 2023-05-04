package com.b305.buddy.extension

import android.content.Intent
import android.os.Handler
import com.b305.buddy.activity.AuthActivity
import com.b305.buddy.activity.MainActivity
import com.b305.buddy.activity.SplashActivity
import com.b305.buddy.model.Token
import com.b305.buddy.model.User
import com.b305.buddy.util.SharedManager

fun SplashActivity.checkIsSavedUser(sharedManager: SharedManager): Boolean {
    val token: Token = sharedManager.getCurrentToken()
    val accessToken = token.accessToken
    val refreshToken = token.refreshToken
    val user: User = sharedManager.getCurrentUser()
    val nickname = user.nickname
    val password = user.password


    return !(accessToken == "" || refreshToken == "" || nickname == "" || password == "")
}

fun SplashActivity.moveMain() {
    Handler().postDelayed({
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }, 1000)
}

fun SplashActivity.moveAuth() {
    startActivity(Intent(this, AuthActivity::class.java))
}
