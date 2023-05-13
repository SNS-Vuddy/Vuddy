package com.b305.vuddy.util

import android.content.Context
import android.content.SharedPreferences
import com.b305.vuddy.model.Token
import com.b305.vuddy.model.User
import com.b305.vuddy.util.PreferenceHelper.get
import com.b305.vuddy.util.PreferenceHelper.remove
import com.b305.vuddy.util.PreferenceHelper.set

class SharedManager(context: Context) {

    private val prefs: SharedPreferences = PreferenceHelper.defaultPrefs(context)

    fun saveCurrentToken(token: Token) {
        prefs["accessToken"] = token.accessToken.toString()
        prefs["refreshToken"] = token.refreshToken.toString()
    }

    fun getCurrentToken(): Token {
        return Token().apply {
            accessToken = prefs["accessToken", ""]
            refreshToken = prefs["refreshToken", ""]
        }
    }

    fun removeCurrentToken() {
        prefs.remove("accessToken")
        prefs.remove("refreshToken")
    }

    fun saveCurrentUser(user: User) {
        prefs["nickname"] = user.nickname.toString()
        prefs["password"] = user.password.toString()
        prefs["profileImgUrl"] = user.profileImgUrl.toString()
        prefs["statusImgUrl"] = user.statusImgUrl.toString()
    }

    fun getCurrentUser(): User {
        return User().apply {
            nickname = prefs["nickname", ""]
            password = prefs["password", ""]
            profileImgUrl = prefs["profileImgUrl", ""]
            statusImgUrl = prefs["statusImgUrl", ""]
        }
    }

    fun removeCurrentUser() {
        prefs.remove("nickname")
        prefs.remove("password")
        prefs.remove("profileImgUrl")
        prefs.remove("statusImgUrl")
    }
}
