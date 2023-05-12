package com.b305.vuddy.model

data class ChatRoomList(
    var chatRoomList: ArrayList<ChatRoom>,
)

data class ChatRoom(
    val profileImage: String, val chatId: Int, val nickname: String, val message: String, val time: String,
)
