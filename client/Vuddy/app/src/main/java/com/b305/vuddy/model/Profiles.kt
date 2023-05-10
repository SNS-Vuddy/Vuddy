package com.b305.vuddy.model

data class FriendProfile(val profileImage: String, val nickname: String)

data class ChatProfile(val chatId: Int, val nickname: String, val lastChat: String, val time: String)
