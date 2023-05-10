package com.b305.vuddy.util

import android.content.Context
import android.content.SharedPreferences
import com.b305.vuddy.model.Token
import com.b305.vuddy.model.User
import com.b305.vuddy.model.UserLocation
import com.b305.vuddy.util.PreferenceHelper.get
import com.b305.vuddy.util.PreferenceHelper.remove
import com.b305.vuddy.util.PreferenceHelper.set
import com.google.gson.Gson

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
    }

    fun getCurrentUser(): User {
        return User().apply {
            nickname = prefs["nickname", ""]
            password = prefs["password", ""]
        }
    }

    fun removeCurrentUser() {
        prefs.remove("nickname")
        prefs.remove("password")
    }

    private fun makeUserLocationList() {
        val userLocationList = ArrayList<UserLocation>()
        val jsonString = Gson().toJson(userLocationList)
        prefs["userLocationList"] = jsonString
    }

    fun addUserLocationList(userLocation: UserLocation) {
        val nickname = userLocation.nickname
        val userLocationList = getUserLocationList().toMutableList()
        userLocationList.removeAll { it.nickname == nickname } // 중복된 nickname 제거
        userLocationList.add(userLocation)
        val jsonString = Gson().toJson(userLocationList)
        prefs["userLocationList"] = jsonString
    }

    fun getUserLocationList(): List<UserLocation> {
        val jsonString = prefs["userLocationList", ""]
        if (jsonString == "") {
            makeUserLocationList()
            return getUserLocationList()
        }
        return Gson().fromJson(jsonString, Array<UserLocation>::class.java).toList()
    }

    fun removeUserInLocationList(nickname: String) {
        val userLocationList = getUserLocationList().toMutableList()
        userLocationList.removeAll { it.nickname == nickname }
        val jsonString = Gson().toJson(userLocationList)
        prefs["userLocationList"] = jsonString
    }

    fun removeUserLocationList() {
        prefs.remove("userLocationList")
    }
}
