package com.b305.vuddy.util

import com.b305.vuddy.App
import com.b305.vuddy.service.AuthService
import com.b305.vuddy.service.FeedService
import com.b305.vuddy.service.FriendService
import com.b305.vuddy.service.UserService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAPI {
    private const val BASE_URL = "http://k8b305.p.ssafy.io"

    class TokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
//            val token  = SharedManager.getCurrentToken().accessToken
//            val token = App.instance.getCurrentToken().accessToken
            val token =
                "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkR1Z6ZERFPSIsImF1dGgiOiJOT1JNQUxfVVNFUiIsImV4cCI6MzE3MjYwMDAwMzgzfQ.Rha49Qpn8aKaDK8WP3xhiNfm7RBTQqNxfvllC0xz0l1OkyuU2oM03w4biID4hAJBHlqG12Wkm9lphBWAnsvqmg"
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token") // API 요청 헤더에 토큰 추가
//                .addHeader("Authorization", token)
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
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

//    private val retrofitfeed: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(okHttpClient)
//            .build()
//    }

    val authService: AuthService by lazy {
        retrofit.create(AuthService::class.java)
    }

    val friendService: FriendService by lazy {
        retrofit.create(FriendService::class.java)
    }
    val feedService: FeedService by lazy {
        retrofit.create((FeedService::class.java))
    }
    val userService: UserService by lazy {
        retrofit.create((UserService::class.java))
    }
}
