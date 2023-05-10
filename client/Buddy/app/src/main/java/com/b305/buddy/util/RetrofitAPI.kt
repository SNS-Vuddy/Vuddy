package com.b305.buddy.util

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import com.b305.buddy.App
import com.b305.buddy.service.AuthService
import com.b305.buddy.service.FeedService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitAPI {
    private const val BASE_URL = "http://k8b305.p.ssafy.io"

//    private val SharedManager: SharedManager by lazy { SharedManager(requireContext()) }
//    private val SharedManager : SharedManager = context.getCurrentToken("prefs_name", Context.MODE_PRIVATE)
    class TokenInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
//            val token  = SharedManager.getCurrentToken().accessToken
//            val token = App.instance.getCurrentToken().accessToken
            val token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJkR1Z6ZERFPSIsImF1dGgiOiJOT1JNQUxfVVNFUiIsImV4cCI6MzE3MjU5NjgyMzgwfQ.W58XBMm7yaSENsSGXNv3j94b725nE_DmXIGbs9SWztpKnWRqhgwsyWrL8Y8hr8AcgQcaBYlMKXSwIER8auRV0Q"
            val request = chain.request().newBuilder()
//                .addHeader("Authorization", "Bearer $token") // API 요청 헤더에 토큰 추가
                .addHeader("Authorization", token)
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

    val feedServie : FeedService by lazy {
        retrofit.create((FeedService::class.java))
    }

}
