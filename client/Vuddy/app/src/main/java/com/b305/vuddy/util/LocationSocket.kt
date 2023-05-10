//package com.b305.vuddy.util
//
//import android.content.Context
//import android.util.Log
//import com.b305.vuddy.model.UserLocation
//import com.b305.vuddy.model.LocationEvent
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okhttp3.Response
//import okhttp3.WebSocket
//import okhttp3.WebSocketListener
//import okio.ByteString
//import org.greenrobot.eventbus.EventBus
//import org.json.JSONObject
//import java.time.LocalDateTime
//
//class LocationSocket(context: Context) {
//    private var client = OkHttpClient()
//    private var url = "ws://k8b305.p.ssafy.io/location"
//    private val sharedManager: SharedManager by lazy { SharedManager(context) }
//    private lateinit var webSocket: WebSocket
//
//    fun connection() {
//        val request = Request.Builder()
//            .url(url)
//            .build()
//
//        webSocket = client.newWebSocket(request, object : WebSocketListener() {
//            override fun onOpen(webSocket: WebSocket, response: Response) {
//                Log.d("LocationSocket", "****onOpen****")
//            }
//
//            override fun onMessage(webSocket: WebSocket, text: String) {
//                val jsonObject = JSONObject(text)
//                val nickname = jsonObject.getString("nickname")
//                val latitude = jsonObject.getString("latitude")
//                val longitude = jsonObject.getString("longitude")
//
//                val userLocation = UserLocation()
//                userLocation.nickname = nickname
//                userLocation.lat = latitude.toDouble().toString()
//                userLocation.lng = longitude.toDouble().toString()
//
//                EventBus.getDefault().post(LocationEvent(false, userLocation))
//                Log.d("LocationSocket", "****onMessage $nickname $latitude $longitude****")
//            }
//
//            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
//                // 서버에서 바이트 메시지를 수신했을 때 실행되는 코드를 작성합니다.
//            }
//
//            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//                // 연결 실패 시 실행되는 코드를 작성합니다.
//            }
//        })
//    }
//
//    fun sendLocation(latitude: String, longitude: String) {
//        val jsonObject = JSONObject()
//            .put("accessToken", sharedManager.getCurrentToken().accessToken.toString())
//            .put("nickname", sharedManager.getCurrentUser().nickname.toString())
//            .put("latitude", latitude)
//            .put("longitude", longitude)
//            .put("localDateTime", LocalDateTime.now().toString())
//        webSocket.send(jsonObject.toString())
//        Log.d("LocationSocket", "****sendLocation $latitude $longitude****")
//    }
//
//    fun disconnect() {
//        webSocket.close(1000, "disconnect")
//        Log.d("LocationSocket", "****disconnect****")
//    }
//}
