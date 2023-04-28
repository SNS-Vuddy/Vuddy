package com.b305.buddy.data

import android.content.Context
import android.content.SharedPreferences
import com.b305.buddy.data.PreferenceHelper.get
import com.b305.buddy.data.PreferenceHelper.set

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
}