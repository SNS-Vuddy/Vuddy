package com.b305.vuddy.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.b305.vuddy.model.Chat
import com.b305.vuddy.model.ChatList
import com.b305.vuddy.model.Token
import com.b305.vuddy.model.User
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

    fun saveChatRoomList(chatList: ArrayList<Chat>) {
        val jsonString = Gson().toJson(chatList)
        prefs["chatRoomList"] = jsonString
    }

    fun updataChatRoomList(chatRoom: Chat) {
        Log.d("share", "!!!!!update $chatRoom")
        val updateList = ArrayList<Chat>()
        val chatId = chatRoom.chatId
        val chatRoomList = getChatRoomList().toMutableList()

        val roomData = chatRoomList.find { it.chatId == chatId }

        // chatId를 가진 인자를 삭제합니다.
        chatRoomList.removeAll { it.chatId == chatId }

        val updateRoomData = Chat().apply {
            profileImage = roomData?.profileImage
            this.chatId = chatId
            nickname = roomData?.nickname
            message = chatRoom.message
            time = chatRoom.time
        }

        updateList.add(updateRoomData)
        updateList.addAll(chatRoomList)
        Log.d("share", "!!!!!update $updateList")

        val jsonString = Gson().toJson(updateList)
        prefs["chatRoomList"] = jsonString
    }


    fun getChatRoomList(): ArrayList<Chat> {
        val jsonString = prefs["chatRoomList", ""]
        val array = Gson().fromJson(jsonString, Array<Chat>::class.java)
        return if (array != null) {
            ArrayList(array.toList())
        } else {
            ArrayList()
        }
    }

    fun removeChatRoomList() {
        prefs.remove("chatRoomList")
    }

    fun saveChatList(chatList: ChatList) {
        prefs["chatId"] = chatList.chatId.toString()
        val jsonString = Gson().toJson(chatList.chatList)
        prefs["chatList"] = jsonString
    }

    fun addChat(chat: Chat) {
        Log.d("share", "!!!!!update $chat")
        val chatId = chat.chatId
        val chatList = getChatList().toMutableList()
        Log.d("share", "!!!!!getChatLsit $chatList")

        if (getChatId() == chatId) {
            chatList.add(chat)
            Log.d("share", "!!!!!addChatList $chatList")
            val jsonString = Gson().toJson(chatList)
            prefs["chatList"] = jsonString
        }
    }

    fun getChatId(): Int {
        Log.d("share", "!!!!ID ${prefs["chatId", ""]}")
        return prefs["chatId", ""].toInt()
    }

    fun getChatList(): ArrayList<Chat> {
        val jsonString = prefs["chatList", ""]
        val array = Gson().fromJson(jsonString, Array<Chat>::class.java)
        return if (array != null) {
            ArrayList(array.toList())
        } else {
            ArrayList()
        }
    }

    fun removeChatList() {
        prefs.remove("chatList")
    }
}
