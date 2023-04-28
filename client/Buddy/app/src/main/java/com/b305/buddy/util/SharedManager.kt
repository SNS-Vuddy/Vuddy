package com.b305.buddy.util

import android.content.Context
import android.content.SharedPreferences
import com.b305.buddy.model.Token
import com.b305.buddy.util.PreferenceHelper.get
import com.b305.buddy.util.PreferenceHelper.remove
import com.b305.buddy.util.PreferenceHelper.set

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
}