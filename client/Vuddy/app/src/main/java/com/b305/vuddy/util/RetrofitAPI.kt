package com.b305.vuddy.util

import com.b305.vuddy.service.AuthService
import com.b305.vuddy.service.FriendService
import okhttp3.OkHttpClient
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor
import com.b305.vuddy.App

object RetrofitAPI {
    private const val BASE_URL = "http://k8b305.p.ssafy.io"

    class TokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
//            val token  = SharedManager.getCurrentToken().accessToken
//            val token = App.instance.getCurrentToken().accessToken
            val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkR1Z6ZERFPSIsImF1dGgiOiJOT1JNQUxfVVNFUiIsImV4cCI6MzE3MjU5Njc5MDQzfQ.LJnIf8Twquxc26wRGALyPvU3vqnKrpexoEV8OaczKtPR8XIDtqOnC9h41p823K9kjmTE37Z2bfJulo02pp17QA"
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
}
