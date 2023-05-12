package com.b305.vuddy.model

data class ChatList(
    var chatList: ArrayList<Chat>,
)


class Chat(
    val chatId: Int, val nickname: String, val message:String, val profileImage:String, val time:String,
)
