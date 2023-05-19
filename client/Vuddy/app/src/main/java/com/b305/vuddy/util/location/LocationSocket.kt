package com.b305.vuddy.util.location

import android.content.Context
import android.util.Log
import com.b305.vuddy.model.UserLocation
import com.b305.vuddy.util.SharedManager
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.time.LocalDateTime

class LocationSocket(context: Context) {
    private var client = OkHttpClient()
//    private var url = "ws://k8b305.p.ssafy.io/location"
    private var url = "ws://43.202.25.203/location"
    private val sharedManager: SharedManager by lazy { SharedManager(context) }
    private lateinit var webSocket: WebSocket
    var isConnected = false

    fun connection() {
        val request = Request.Builder()
            .url(url)
            .build()

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("LocationSocket", "****onOpen****")
                isConnected = true
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val jsonObject = JSONObject(text)
                val nickname = jsonObject.getString("nickname")
                val latitude = jsonObject.getString("latitude")
                val longitude = jsonObject.getString("longitude")
                val statusImgUrl = jsonObject.getString("status")
                val profileImgUrl = jsonObject.getString("imgUrl")

                val userLocation = UserLocation()
                userLocation.nickname = nickname
                userLocation.latitude = latitude
                userLocation.longitude = longitude
                userLocation.statusImgUrl = statusImgUrl
                userLocation.profileImgUrl = profileImgUrl

                val user = sharedManager.getCurrentUser()
                user.statusImgUrl = statusImgUrl
                sharedManager.saveCurrentUser(user)

                EventBus.getDefault().post(userLocation)
                Log.d("LocationSocket", "****onMessage $nickname $latitude $longitude $statusImgUrl $profileImgUrl****")
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                // 서버에서 바이트 메시지를 수신했을 때 실행되는 코드를 작성합니다.
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                // 연결 실패 시 실행되는 코드를 작성합니다.
            }
        })
    }

    fun sendLocation(userLocation: UserLocation) {
        if (!isConnected) {
            return
        }
        val nickname = userLocation.nickname
        val latitude = userLocation.latitude
        val longitude = userLocation.longitude
        val profileImgUrl = userLocation.profileImgUrl

        val jsonObject = JSONObject()
            .put("accessToken", sharedManager.getCurrentToken().accessToken.toString())
            .put("nickname", nickname)
            .put("latitude", latitude)
            .put("longitude", longitude)
            .put("localDateTime", LocalDateTime.now().toString())
            .put("imgUrl", profileImgUrl)
        webSocket.send(jsonObject.toString())
        Log.d("LocationSocket", "****sendLocation $latitude $longitude $profileImgUrl****")
    }

    fun disconnect() {
        isConnected = false
        webSocket.close(1000, "disconnect")
        Log.d("LocationSocket", "****disconnect****")
    }
}
