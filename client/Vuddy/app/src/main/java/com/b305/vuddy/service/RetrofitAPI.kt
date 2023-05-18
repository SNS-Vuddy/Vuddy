package com.b305.vuddy.service

import com.b305.vuddy.model.App
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAPI {

    // java.net.SocketTimeoutException
//    private const val BASE_URL = "http://k8b305.p.ssafy.io"
    // java.net.UnknownHostException
    private const val BASE_URL = "http://www.vuddy.co.kr"

    class TokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val token = App.instance.getCurrentToken().accessToken
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token") // API 요청 헤더에 토큰 추가
                .build()
            return chain.proceed(request)
        }
    }

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(TokenInterceptor())
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val friendService: FriendService by lazy {
        retrofit.create(FriendService::class.java)
    }

    val feedService: FeedService by lazy {
        retrofit.create((FeedService::class.java))
    }

    val mapFeedService: MapFeedService by lazy {
        retrofit.create((MapFeedService::class.java))
    }

    val userService: UserService by lazy {
        retrofit.create((UserService::class.java))
    }
}
