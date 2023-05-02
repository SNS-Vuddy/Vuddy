package com.b305.buddy.util

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.b305.buddy.databinding.ActivitySocketBinding
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject
import java.time.LocalDateTime

class SocketActivity : AppCompatActivity() {
    private var client = OkHttpClient()
    private var url = "ws://192.168.31.14:8080/chat"
    private lateinit var binding: ActivitySocketBinding
    private val sharedManager: SharedManager by lazy { SharedManager(this) }
    private lateinit var webSocket: WebSocket
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySocketBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setButton()
    }
    
    private fun setButton() {
        binding.btnConnect.setOnClickListener {
            connection()
        }
        
        binding.btnChat.setOnClickListener {
            var jsonObject = JSONObject()
                .put("accessToken", sharedManager.getCurrentToken().accessToken.toString())
                .put("nickname", sharedManager.getCurrentUser().nickname.toString())
                .put("message", binding.etMessage.text.toString())
                .put("localDateTime", LocalDateTime.now().toString())
            
            webSocket.send(jsonObject.toString())
        }
    }
    
    private fun connection() {
        val request = Request.Builder()
            .url(url)
            .build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                // 연결이 성공적으로 열렸을 때 실행되는 코드를 작성합니다.
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
}