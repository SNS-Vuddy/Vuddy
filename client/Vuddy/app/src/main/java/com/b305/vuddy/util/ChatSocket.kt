package com.b305.vuddy.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.b305.vuddy.R
import com.b305.vuddy.model.App
import com.b305.vuddy.model.Chat
import com.b305.vuddy.model.ChatList
import com.b305.vuddy.view.activity.MainActivity
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
//    private var url = "ws://k8b305.p.ssafy.io/chatting"
//    private var url = "ws://43.202.25.203/chatting"
    private var url = "ws://192.168.140.217:8080/chatting"
    private var context: Context = context.applicationContext
    private val sharedManager: SharedManager by lazy { SharedManager(context) }
    private lateinit var webSocket: WebSocket
    private val myNick = App.instance.getCurrentUser().nickname

    var isConnected = false

    fun connection() {
        Log.d("ChatSocket", "****CHATCHAT****")
        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("ChatSocket", "****CHATCHAT****")
                isConnected = true
                loadChatRoomList()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val jsonObject = JSONObject(text)
                Log.d("chatSocket", "!!!!!받았다 $jsonObject")

                when (jsonObject.getString("type")) {
                    "LOAD" -> {
                        val chatRoomList = jsonObject.getJSONArray("innerList")
                        val chatRoomArrayList = ArrayList<Chat>()

                        for (i in 0 until chatRoomList.length()) {
                            val chatObject = chatRoomList.getJSONObject(i)
                            val chat = Chat(
                                chatObject.getString("profileImg"),
                                chatObject.getInt("chatId"),
                                chatObject.getString("nickname"),
                                chatObject.getString("lastChat"),
                                chatObject.getString("time"),
                            )
                            chatRoomArrayList.add(chat)
                        }

                        sharedManager.saveChatRoomList(chatRoomArrayList)
                    }

                    "OPEN" -> {
                        val resData = jsonObject.getJSONObject("data")
                        val chatId = resData.getInt("chatId")
                        val chatArrayList = ArrayList<Chat>()
                        val chatLsit = ChatList(chatId, chatArrayList)

                        sharedManager.saveChatList(chatLsit)
                    }

                    "JOIN" -> {
                        val resData = jsonObject.getJSONObject("data")
                        val chatId = resData.getInt("chatId")
                        val chatList = resData.getJSONArray("messageList")
                        val chatArrayList = ArrayList<Chat>()
                        for (i in 0 until chatList.length()) {
                            val chatObject = chatList.getJSONObject(i)
                            val chat = Chat(
                                chatObject.getString("profileImg"),
                                chatObject.getInt("chatId"),
                                chatObject.getString("nickname"),
                                chatObject.getString("message"),
                                chatObject.getString("time"),
                            )
                            chatArrayList.add(chat)
                        }
                        val chatLsit = ChatList(chatId, chatArrayList)

                        sharedManager.saveChatList(chatLsit)
                    }

                    "CHAT" -> {
                        val resData = jsonObject.getJSONObject("data")
                        val profileImg = resData.getString("profileImg")
                        val chatId = resData.getInt("chatId")
                        val nickname = resData.getString("nickname")
                        val message = resData.getString("message")
                        val time = resData.getString("time")

                        val chat = Chat()
                        chat.profileImage = profileImg
                        chat.chatId = chatId
                        chat.nickname = nickname
                        chat.message = message
                        chat.time = time


                        sharedManager.updataChatRoomList(chat)
                        sharedManager.addChat(chat)

                        EventBus.getDefault().post(chat)

                        showNotification(nickname, message)
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
            .put("nickname1", myNick)
            .put("type", "LOAD")

        webSocket.send(jsonObject.toString())
        Log.d("ChatSocket", "****sendMessage LOAD!!!!!!!!!!****")
    }

    fun sendMessage(message: String) {
        if (!isConnected) {
            return
        }
        val jsonObject = JSONObject()
            .put("nickname1", myNick)
            .put("chatId", App.instance.getChatId())
            .put("type", "CHAT")
            .put("message", message)

        webSocket.send(jsonObject.toString())
        Log.d("ChatSocket", "****sendMessage $message****")
    }

    fun goChatting(nickname: String?) {
        if (!isConnected) {
            return
        }
        val chatRoomList: ArrayList<Chat> = App.instance.getChatRoomList()
        if (chatRoomList.any { it.nickname == nickname }) {
            val jsonObject = JSONObject()
                .put("nickname1", myNick)
                .put("nickname2", nickname)
                .put("type", "JOIN")

            webSocket.send(jsonObject.toString())
            Log.d("ChatSocket", "****sendMessage JOIN!!!!!!****")
        } else {
            val jsonObject = JSONObject()
                .put("nickname1", myNick)
                .put("nickname2", nickname)
                .put("type", "OPEN")

            webSocket.send(jsonObject.toString())
            Log.d("ChatSocket", "****sendMessage OPEN!!!!!!****")
        }
    }

    private fun showNotification(nickname: String, message: String) {
        // 알림 생성 및 설정
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "chat_notification_channel"
        val channelName = "Chat Notification"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(channel)

        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            action = "com.b305.vuddy.MESSAGE_FRAGMENT_ACTION"
            putExtra("nickname", nickname)
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(nickname)
            .setContentText(message)
            .setSmallIcon(R.drawable.bird)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // 알림 표시
        notificationManager.notify(0, notification)
    }


    fun disconnect() {
        isConnected = false
        webSocket.close(1000, "disconnect")
        Log.d("ChatSocket", "****disconnect****")
    }
}
