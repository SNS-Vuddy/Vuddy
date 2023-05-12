package com.b305.vuddy.util

import android.content.Context
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject
import java.time.LocalDateTime

class ChatSocket(context: Context) {
    private var client = OkHttpClient()
    private var url = "ws://k8b305.p.ssafy.io/chatting"
    private val sharedManager: SharedManager by lazy { SharedManager(context) }
    private lateinit var webSocket: WebSocket

    fun connection() {
        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("ChatSocket", "****onOpen****")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                // 서버에서 텍스트 메시지를 수신했을 때 실행되는 코드를 작성합니다.
                Log.d("onMessage", text)
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // 서버에서 바이트 메시지를 수신했을 때 실행되는 코드를 작성합니다.
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // 연결 실패 시 실행되는 코드를 작성합니다.
            }
        })
    }

    fun sendMessage(chatId: Int, message: String) {
        var jsonObject = JSONObject()
            .put("nickname", sharedManager.getCurrentUser().nickname.toString())
            .put("type", "CHAT")
            .put("chatId", chatId)
            .put("message", message)
            .put("time", LocalDateTime.now().toString())

        webSocket.send(jsonObject.toString())
    }

    fun disconnect() {
        webSocket.close(1000, "disconnect")
        Log.d("ChatSocket", "****disconnect****")
    }
}
