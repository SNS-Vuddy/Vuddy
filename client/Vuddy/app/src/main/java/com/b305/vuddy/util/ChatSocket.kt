package com.b305.vuddy.util

import android.content.Context
import android.util.Log
import com.b305.vuddy.model.Chat
import com.b305.vuddy.model.ChatList
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject

class ChatSocket(context: Context) {
    private var client = OkHttpClient()
    private var url = "ws://develop.vuddy.co.kr/chatting"
    private val sharedManager: SharedManager by lazy { SharedManager(context) }
    private lateinit var webSocket: WebSocket
    var isConnected = false

    fun connection() {
        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                isConnected = true
                loadChatRoomList()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val jsonObject = JSONObject(text)
                val type = jsonObject.getString("type")

                when (type) {
                    "LOAD" -> {
                        val chatRoomList = jsonObject.getJSONArray("chatroomList")
                        val chatRoomArrayList = ArrayList<Chat>()

                        for (i in 0 until chatRoomList.length()) {
                            val chatObject = chatRoomList.getJSONObject(i)
                            val chat = Chat(
                                chatObject.getString("profileImage"),
                                chatObject.getInt("chatId"),
                                chatObject.getString("nickname"),
                                chatObject.getString("message"),
                                chatObject.getString("time"),
                            )
                            chatRoomArrayList.add(chat)
                        }
                        sharedManager.saveChatRoomList(chatRoomArrayList)
                    }

                    "OPEN" -> {
                        val resData = jsonObject.getJSONArray("data")
                        val chatId = resData.getInt(3)
                        val chatArrayList = ArrayList<Chat>()
                        val chatLsit: ChatList = ChatList(chatId, chatArrayList)
                        sharedManager.saveChatList(chatLsit)
                    }

                    "JOIN" -> {
                        val resData = jsonObject.getJSONArray("data")
                        val chatId = resData.getInt(3)
                        val chatList = resData.getJSONArray(2)
                        val chatArrayList = ArrayList<Chat>()
                        for (i in 0 until chatList.length()) {
                            val chatObject = chatList.getJSONObject(i)
                            val chat = Chat(
                                chatObject.getString("profileImage"),
                                chatObject.getInt("chatId"),
                                chatObject.getString("nickname"),
                                chatObject.getString("message"),
                                chatObject.getString("time"),
                            )
                            chatArrayList.add(chat)
                        }
                        val chatLsit: ChatList = ChatList(chatId, chatArrayList)
                        sharedManager.saveChatList(chatLsit)
                    }

                    "Chat" -> {
                        val profileImage = jsonObject.getString("profileImage")
                        val chatId = jsonObject.getInt("chatId")
                        val nickname = jsonObject.getString("nickname")
                        val message = jsonObject.getString("message")
                        val time = jsonObject.getString("time")

                        val chat = Chat()
                        chat.profileImage = profileImage
                        chat.chatId = chatId
                        chat.nickname = nickname
                        chat.message = message
                        chat.time = time

                        sharedManager.updataChatRoomList(chat)
                        sharedManager.addChat(chat)

                        EventBus.getDefault().post(chat)
                        Log.d("onMessage", text)
                    }
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // 서버에서 바이트 메시지를 수신했을 때 실행되는 코드를 작성합니다.
                Log.d("ChatSocket", "****sendMessage 받았다****")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // 연결 실패 시 실행되는 코드를 작성합니다.
            }
        })
    }

    fun loadChatRoomList() {
        if (!isConnected) {
            return
        }
        val jsonObject = JSONObject()
            .put("nickname1", sharedManager.getCurrentUser().nickname.toString())
            .put("type", "LOAD")

        webSocket.send(jsonObject.toString())
    }

    fun sendMessage(chatId: Int, message: String) {
        if (!isConnected) {
            return
        }
        val jsonObject = JSONObject()
            .put("nickname1", sharedManager.getCurrentUser().nickname.toString())
            .put("chatId", chatId)
            .put("type", "CHAT")
            .put("message", message)

        webSocket.send(jsonObject.toString())
        Log.d("ChatSocket", "****sendMessage $chatId $message****")
    }

    fun disconnect() {
        isConnected = false
        webSocket.close(1000, "disconnect")
        Log.d("ChatSocket", "****disconnect****")
    }
}
